package edu.nyu.engineering.vida.kvdb4j.rocksdb;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import edu.nyu.engineering.vida.kvdb4j.api.BytesBytesIterator;
import edu.nyu.engineering.vida.kvdb4j.api.KVDB;
import java.io.Closeable;
import java.io.File;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class RocksDBBackend implements Closeable, KVDB {

  protected Options options;
  protected RocksDB db;

  static {
    RocksDB.loadLibrary();
  }

  public RocksDBBackend(RocksDB db, Options options) {
    this.db = db;
    this.options = options;
  }

  public static RocksDBBackend create(String path, boolean readonly) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
    Options options = new Options();
    options.setCreateIfMissing(true);
    try {
      RocksDB db;
      if (readonly) {
        db = RocksDB.openReadOnly(options, path);
      } else {
        db = RocksDB.open(options, path);
      }
      return new RocksDBBackend(db, options);
    } catch (RocksDBException e) {
      String message =
          String.format(
              "Failed to open/create RocksDB database at %s. Error code: %s",
              path, e.getStatus().getCodeString());
      throw new RuntimeException(message, e);
    }
  }

  public void putBytes(byte[] keyBytes, byte[] valueBytes) {
    try {
      db.put(keyBytes, valueBytes);
    } catch (RocksDBException e) {
      String hexKey = BaseEncoding.base16().encode(keyBytes);
      throw new RuntimeException("Failed to write key to database: " + hexKey);
    }
  }

  public byte[] getBytes(byte[] keyBytes) {
    Preconditions.checkNotNull(this.db, "Make sure the database is open.");
    byte[] valueBytes;
    try {
      valueBytes = db.get(keyBytes);
    } catch (RocksDBException e) {
      String hexKey = BaseEncoding.base16().encode(keyBytes);
      throw new RuntimeException("Failed to get value from database for key: " + hexKey, e);
    }
    return valueBytes;
  }

  @Override
  public synchronized void close() {
    if (db != null) {
      db.close();
      db = null;
      options.close();
      options = null;
    }
  }

  public BytesBytesIterator createIterator() {
    return new RocksDBIterator(this.db);
  }
}
