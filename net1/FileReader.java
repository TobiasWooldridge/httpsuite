package net1;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

class FileReader {

    String path;

    FileReader(String path) {
        this.path = path;
    }

    String read() throws Exception {
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
