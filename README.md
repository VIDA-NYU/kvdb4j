# KVDB4J

A simple Java interface for multiple key-value databases.


## Usage

#### Creating a key-value DB (KVDB) instance

The  first step is to create a key-value DB instance. Currently,
[RocksDB](https://github.com/facebook/rocksdb/) and
[LevelDB](https://github.com/dain/leveldb) implementations are available.
```java
// RocksDB
boolean readOnly = false;
KVDB db = RocksDBBackend.create(path, readOnly);
// LevelDB
KVDB db = LevelDBBackend.create(path)
```

#### Storing and reading data

The KVDB interface only allows storing raw byte arrays. To store higher-level
data easily, the `StringObjectKVDB` class can be used to automatically serialize
and store Java objects. `StringObjectKVDB` we transform objects into bytes and
store them using the underlying `KVDB` implementation provided.

In the following example, objects are automatically serialized using the Kryo library:
```java
StringObjectKVDB<String> odb = new StringObjectKVDB<>(db, new KryoIO<>(String.class));

odb.put("a", "1");
odb.put("b", "2");

assertEquals("1", odb.get("a"));
assertEquals("2", odb.get("b"));

try(CloseableIterator<KV<String, String>> it = odb.iterator()){

  assertTrue(it.hasNext());
  
  KV<String, String> next;
  next = it.next();
  assertEquals("a", next.getKey());
  assertEquals("1", next.getValue());
  
  assertTrue(it.hasNext());
  next = it.next();
  assertEquals("b", next.getKey());
  assertEquals("2", next.getValue());
  
  assertFalse(it.hasNext());
  assertNull(it.next());

} catch (Exception e) {
  throw new RuntimeException("Failed to close the database iterator", e);
}

odb.close();
```
Instead of using Kryo serialization, users can configure custom
serialization strategies by providing an object that implements
the `IO` interface (`KryoIO` implements this interface).

#### KVDB for specific data types

Storing native data types is also possible using specialized classes:
- `StringIntKVDB`, key is `String` and value is `int`.
- `IntStringKVDB`, key is `int` and value is `String`.
- `BytesBytesKVDB`, key is `byte[]` and value is `byte[]`.
- `IntStringKVDB`, key is `int` and value is `String`.
- `StringObjectKVDB`, key is `String` and value is `Object`.
