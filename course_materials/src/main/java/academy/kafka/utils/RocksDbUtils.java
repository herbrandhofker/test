package academy.kafka.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class RocksDbUtils {

    static public void removeDatabase(String name) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(tmpDir, name + ".db");
        try {
            Options options = new Options();
            RocksDB.destroyDB(path.toString(), options);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    static public RocksDB newDatabase(String name) {
        removeDatabase(name);
        return openDatabase(name);
    }

    static public RocksDB openDatabase(String name) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(tmpDir, name + ".db");
        Options options = new Options();
        options.setCreateIfMissing(true);
        try {
            return RocksDB.open(options, path.toString());
        } catch (RocksDBException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> void loadFromKeyValueFile(String topic, Function<String, T> fromJson) {
        URL resource = RocksDbUtils.class.getClassLoader().getResource(topic + ".key_value");
        RocksDB db = newDatabase(topic);

        if (resource == null) {
            throw new IllegalArgumentException("file " + topic + ".key_value not found!");
        } else {
            try {
                File kvFile = new File(resource.toURI());
                try (BufferedReader br = new BufferedReader(new FileReader(kvFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty())
                            continue;
                        String key = line.substring(0, line.indexOf(":"));
                        String jsonStr = line.substring(line.indexOf(":") + 1);

                        db.put(key.getBytes(), jsonStr.getBytes());
                    }
                } catch (RocksDBException e) {
                    e.printStackTrace();
                }
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
        db.close();
    }
}