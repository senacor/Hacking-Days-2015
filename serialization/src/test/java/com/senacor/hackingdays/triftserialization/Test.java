package com.senacor.hackingdays.triftserialization;

import com.senacor.hackingdays.serialization.thirftdata.Gender;
import com.senacor.hackingdays.serialization.thirftdata.Profile;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

/**
 * Created with IntelliJ IDEA.
 * User: lrscholz
 * Date: 18/09/15
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
public class Test {
  @org.junit.Test
  public void simple() throws TException {

    Profile work = new Profile();

    work.setName("test");
    work.setGender(Gender.Male);

    TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
    byte[] bytes = serializer.serialize(work);

    TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
    Profile moreWork = new Profile();
    deserializer.deserialize(moreWork, bytes);

  }
}
