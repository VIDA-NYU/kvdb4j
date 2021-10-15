package edu.nyu.engineering.vida.kvdb4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.nyu.engineering.vida.kvdb4j.api.CloseableIterator;
import edu.nyu.engineering.vida.kvdb4j.api.KV;
import edu.nyu.engineering.vida.kvdb4j.api.KVDB;
import edu.nyu.engineering.vida.kvdb4j.api.StringObjectKVDB;
import edu.nyu.engineering.vida.kvdb4j.leveldb.LevelDBBackend;
import edu.nyu.engineering.vida.kvdb4j.rocksdb.RocksDBBackend;
import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class KvdbBackendTest {

  @TempDir File tmpDir;

  static Stream<Arguments> backendImplementations() {
    Function<String, KVDB> rocksdb = (String path) -> RocksDBBackend.create(path, false);
    Function<String, KVDB> leveldb = (String path) -> LevelDBBackend.create(path);
    return Stream.of(Arguments.of(rocksdb, "rocksdb"), Arguments.of(leveldb, "leveldb"));
  }

  @DisplayName("Test insert and reads on String/Object KVDB backends")
  @ParameterizedTest(name = "{index}: {1}")
  @MethodSource("backendImplementations")
  void testInsertReadStringObjectKVDB(Function<String, RocksDBBackend> builder, String dbname) {
    KVDB db = builder.apply(tmpDir.toString());
    StringObjectKVDB<String> odb = new StringObjectKVDB<>(db, new KryoIO<>(String.class));

    odb.put("a", "1");
    odb.put("b", "2");

    assertEquals("1", odb.get("a"));
    assertEquals("2", odb.get("b"));

    CloseableIterator<KV<String, String>> it = odb.iterator();

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

    odb.close();
  }
}
