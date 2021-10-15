package edu.nyu.engineering.vida.kvdb4j.api;

public interface KVDB {

  void putBytes(byte[] keyBytes, byte[] valueBytes);

  byte[] getBytes(byte[] keyBytes);

  void close();

  BytesBytesIterator createIterator();
}
