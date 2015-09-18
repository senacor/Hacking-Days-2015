package com.senacor.hackingdays.serializer;

import akka.serialization.JSerializer;
import com.senacor.hackingdays.serialization.data.*;
import org.nustaq.serialization.FSTConfiguration;

/**
 * Created by hmarginean on 18/09/15.
 */
public class FastSerializer extends JSerializer {
    static FSTConfiguration conf = FSTConfiguration.createFastBinaryConfiguration();
    static {
        conf.registerClass(Activity.class,Gender.class, Location.class, Profile.class, Range.class, RelationShipStatus.class, Seeking.class);
    }


    @Override
    public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
        return conf.asObject(bytes);
    }

    @Override
    public byte[] toBinary(Object o) {
        byte byteArray[] = conf.asByteArray(o);
        return byteArray;
    }

    @Override
    public int identifier() {
        return 1373372;
    }

    @Override
    public boolean includeManifest() {
        return false;
    }
}
