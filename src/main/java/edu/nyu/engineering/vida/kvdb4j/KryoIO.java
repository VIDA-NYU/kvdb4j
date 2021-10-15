package edu.nyu.engineering.vida.kvdb4j;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import edu.nyu.engineering.vida.kvdb4j.api.IO;
import java.io.ByteArrayOutputStream;

/**
 * A thread-safe wrapper for serialization using Kryo.
 *
 * @author aeciosantos
 * @param <T> The type of the object which will be serialized/deserialized.
 */
public class KryoIO<T> implements IO<T> {

  private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(Kryo::new);
  private final Class<T> contentClass;

  public KryoIO(Class<T> contentClass) {
    this.contentClass = contentClass;
  }

  @Override
  public byte[] serialize(T value) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Output output = new Output(baos);
    KRYO.get().writeObject(output, value);
    output.flush();
    return baos.toByteArray();
  }

  @Override
  public T deserialize(byte[] valueBytes) {
    if (valueBytes == null) return null;
    Input input = new Input(valueBytes);
    return KRYO.get().readObject(input, contentClass);
  }
}
