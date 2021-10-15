package edu.nyu.engineering.vida.kvdb4j.api;

public class IntStringKVDB extends AbstractKVDB {

  public IntStringKVDB(KVDB db) {
    super(db);
  }

  public String get(int key) {
    byte[] bytes = getBytes(intToBytes(key));
    if (bytes == null) {
      return null;
    } else {
      return bytesToString(bytes);
    }
  }

  public void put(int key, String value) {
    putBytes(intToBytes(key), stringToBytes(value));
  }
}
