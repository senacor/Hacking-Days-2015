package com.senacor.hackingdays.serialization.data.writer;

import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

public class XMLProfileWriterTest {

    @Rule
    public final TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void thatFileCanBeWritten() throws IOException {

        File file = tmpFolder.newFile();
        try (ProfileWriter writer = new XMLProfileWriter(file)) {
            new ProfileGenerator(2).stream().forEach(writer::write);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try (ProfileReader reader = new XMLProfileReader(file)) {
            reader.stream().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

}