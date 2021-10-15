package edu.nyu.engineering.vida.kvdb4j.api;

public class StringObjectKVDB<V> extends AbstractKVDB implements Iterable<KV<String, V>> {

  private IO<V> io;

  public StringObjectKVDB(KVDB db, IO<V> io) {
    super(db);
    this.io = io;
  }

  public void put(String key, V value) {
    byte[] valueBytes = io.serialize(value);
    byte[] keyBytes = key.getBytes();
    putBytes(keyBytes, valueBytes);
  }

  public V get(String key) {
    byte[] bytes = key.getBytes();
    byte[] valueBytes = getBytes(bytes);
    return io.deserialize(valueBytes);
  }

  @Override
  public KVIterator<String, V> iterator() {
    return new StringObjectIterator(createIterator());
  }

  protected class StringObjectIterator extends IteratorBase<KV<byte[], byte[]>>
      implements KVIterator<String, V> {

    public StringObjectIterator(KVIterator<byte[], byte[]> it) {
      super(it);
    }

    @Override
    public KV<String, V> next() {
      KV<byte[], byte[]> next = it.next();
      if (next == null) {
        return null;
      } else {
        V value = io.deserialize(next.getValue());
        return new KV<>(new String(next.getKey()), value);
      }
    }
  }
}
