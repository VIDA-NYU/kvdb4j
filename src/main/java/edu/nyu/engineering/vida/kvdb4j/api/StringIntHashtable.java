package edu.nyu.engineering.vida.kvdb4j.api;

public class StringIntHashtable extends AbstractKVDB {

  private final int DEFAULT_ABSENT_VALUE = -1;
  private final int absentValue;

  public StringIntHashtable(KVDB db, int absentValue) {
    super(db);
    this.absentValue = absentValue;
  }

  public StringIntHashtable(KVDB db) {
    super(db);
    this.absentValue = DEFAULT_ABSENT_VALUE;
  }

  public int get(String key) {
    byte[] bytes = getBytes(stringToBytes(key));
    if (bytes == null) {
      return DEFAULT_ABSENT_VALUE;
    } else {
      return bytesToInt(bytes);
    }
  }

  public void put(String key, int value) {
    putBytes(stringToBytes(key), intToBytes(value));
  }
}
