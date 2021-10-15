package edu.nyu.engineering.vida.kvdb4j.api;

public class BytesBytesKVDB extends AbstractKVDB implements Iterable<KV<byte[], byte[]>> {

  public BytesBytesKVDB(KVDB db) {
    super(db);
  }

  public void put(byte[] key, byte[] value) {
    putBytes(key, value);
  }

  public byte[] get(byte[] key) {
    return getBytes(key);
  }

  @Override
  public CloseableIterator<KV<byte[], byte[]>> iterator() {
    return super.createIterator();
  }
}
