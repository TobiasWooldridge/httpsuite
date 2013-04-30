package demo;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

class FileReader {
    String read(String path) throws IOException {
        String fileContent;

        FileInputStream stream = new FileInputStream(new File(path));

        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fileContent = Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }

        return fileContent;
    }
}
