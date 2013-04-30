package demo;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

class CachedFileReader extends FileReader {
    private static class CachedFile {
        public volatile boolean updatingFile = true;

        public String path;
        public String content;

        public volatile long timeRead;
        public volatile long lastAccess;
    }

    ConcurrentHashMap<String, CachedFile> fileCache;
    FileReader reader;

    CachedFileReader() {
        reader = new FileReader();
        fileCache = new ConcurrentHashMap<>();
    }

    private void updateCacheFile(CachedFile file) throws IOException {
        synchronized (file) {
            file.timeRead = System.currentTimeMillis();
            file.content = reader.read(file.path);

            file.updatingFile = false;
        
            file.notifyAll();
        }
    }

    private CachedFile cacheFile(String path) throws IOException {
        CachedFile file = fileCache.get(path);

        boolean cacheHit = true;
        if (file == null) {
            file = new CachedFile();

            synchronized(file) {
                file.updatingFile = true;
                file.path = path;

                fileCache.putIfAbsent(path, file);

                // If somebody's already creating the file we want, abort making a
                // new file and use theirs.
                CachedFile hashMapFile = fileCache.get(path);
                if (hashMapFile != file) {
                    file = hashMapFile;
                }
                else {
                    cacheHit = false;
                    updateCacheFile(file);
                }
            }
        }

        // Something's in the cache + we're not the request adding the file
        if (cacheHit) {
            try {
                while (file.updatingFile) {
                    synchronized(file) {
                        if (!file.updatingFile) {
                            break;
                        }
                        else {
                            file.wait();
                        }
                    }
                }
            }
            catch (InterruptedException e) {
            }
        }

        file.lastAccess = System.currentTimeMillis();

        return file;
    }

    @Override
    public String read(String path) throws IOException {
        return cacheFile(path).content;
    }
}
