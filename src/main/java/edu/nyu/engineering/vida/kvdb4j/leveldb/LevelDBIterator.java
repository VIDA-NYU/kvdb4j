package edu.nyu.engineering.vida.kvdb4j.leveldb;

import edu.nyu.engineering.vida.kvdb4j.api.BytesBytesIterator;
import edu.nyu.engineering.vida.kvdb4j.api.KV;
import java.io.IOException;
import java.util.Map.Entry;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;

public class LevelDBIterator implements BytesBytesIterator {

  private final DBIterator cursor;
  private boolean isOpen;
  private byte[] key;
  private final DB db;

  public LevelDBIterator(DB db) {
    this.db = db;
    this.cursor = db.iterator();
    this.cursor.seekToFirst();
    this.isOpen = true;
  }

  @Override
  public void close() {
    if (this.isOpen) {
      try {
        cursor.close();
      } catch (IOException e) {
        throw new RuntimeException("Failed to close LevelDB iterator", e);
      }
    }
  }

  @Override
  public boolean hasNext() {
    return cursor.hasNext();
  }

  @Override
  public KV<byte[], byte[]> next() {
    if (this.isOpen && cursor.hasNext()) {
      Entry<byte[], byte[]> next = cursor.next();
      this.key = next.getKey();
      return new KV<>(this.key, next.getValue());
    } else {
      return null;
    }
  }

  public void remove() {
    try {
      db.delete(key);
    } catch (DBException e) {
      throw new RuntimeException("Failed to remove entry from RocksDB");
    }
  }
}
