package edu.nyu.engineering.vida.kvdb4j.api;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;

public abstract class AbstractKVDB implements Closeable {

  protected final KVDB db;

  public AbstractKVDB(KVDB db) {
    this.db = db;
  }

  protected void putBytes(byte[] keyBytes, byte[] valueBytes) {
    db.putBytes(keyBytes, valueBytes);
  }

  protected byte[] getBytes(byte[] keyBytes) {
    return db.getBytes(keyBytes);
  }

  @Override
  public synchronized void close() {
    db.close();
  }

  /*
   * Converts an int to a byte array using big-endian order.
   */
  static byte[] intToBytes(int value) {
    return new byte[] {
      (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)
    };
  }

  /*
   * Converts a byte array to an int using big-endian order.
   */
  static int bytesToInt(byte[] bytes) {
    return (bytes[0]) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
  }

  static byte[] stringToBytes(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

  static String bytesToString(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }

  protected BytesBytesIterator createIterator() {
    return db.createIterator();
  }
}
