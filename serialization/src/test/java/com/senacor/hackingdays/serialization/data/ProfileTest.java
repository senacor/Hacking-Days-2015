package com.senacor.hackingdays.serialization.data;

import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.fail;

public class ProfileTest {


    @Test
    public void thatProfilesAreJavaSerializable() throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(bos);

        Profile p = ProfileGenerator.newProfile();
        try {
            ous.writeObject(p);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            Profile p2 = (Profile) ois.readObject();
        } catch (IOException e) {
            fail();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
