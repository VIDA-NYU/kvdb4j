package edu.nyu.engineering.vida.kvdb4j.api;

public class KV<K, V> {

  private final K key;
  private final V value;

  public KV(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return this.key;
  }

  public V getValue() {
    return this.value;
  }
}
