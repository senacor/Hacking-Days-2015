package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.senacor.hackingdays.serialization.data.Profile;
import org.eclipse.persistence.jaxb.JAXBContextProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhaunolder on 18.09.15.
 */
public class JAXBSerializer extends JSerializer {

    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;

    private ByteArrayOutputStream result;

    static {
        try {
            Map<String, Object> jaxbProperties = new HashMap<>(2);
            jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
            jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
            JAXBContext jc = JAXBContext.newInstance(new Class[]{Profile.class},
                    jaxbProperties);

            unmarshaller = jc.createUnmarshaller();
            marshaller = jc.createMarshaller();
        } catch (Exception e) {
            // there are no errors!!
        }
    }

    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> aClass) {
        try {
            return unmarshaller.unmarshal(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            // there are no errors!!
        }
        return null;
    }

    @Override
    public byte[] toBinary(Object o) {
        try {
            result = new ByteArrayOutputStream();
            marshaller.marshal(o, result);
            return result.toByteArray();
        } catch (Exception e) {
            // there are no errors!!
        }
        return null;
    }

    @Override
    public boolean includeManifest() {
        return false;
    }

    @Override
    public int identifier() {
        return 1234;
    }
}
