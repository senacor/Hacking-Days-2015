package com.senacor.hackingdays.serializer.thrift;

import akka.serialization.JSerializer;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TTupleProtocol;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 18/09/15
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
public class ThriftSerializerTTuple extends JSerializer {

  private TSerializer serializer = new TSerializer(new TTupleProtocol.Factory());
  TDeserializer deserializer = new TDeserializer(new TTupleProtocol.Factory());

  @Override
  public int identifier() {
    return 4711;
  }

  @Override
  public boolean includeManifest() {
    return false;
  }

  @Override
  public byte[] toBinary(Object o) {
    TBase<?,?> ret = (TBase<?,?>) o;
    try {
      return serializer.serialize(ret);
    } catch (TException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object fromBinaryJava(byte[] payload, Class<?> clazz) {
    TBase<?,?> ret = null;
    try {
      ret = (TBase<?,?>) clazz.newInstance();
      deserializer.deserialize(ret, payload);

    } catch (InstantiationException|IllegalAccessException|TException e) {
      throw new RuntimeException(e);
    }

    return ret;
  }

}
