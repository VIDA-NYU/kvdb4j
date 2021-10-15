package edu.nyu.engineering.vida.kvdb4j.api;

public interface IO<T> {
  byte[] serialize(T value);

  T deserialize(byte[] valueBytes);
}
