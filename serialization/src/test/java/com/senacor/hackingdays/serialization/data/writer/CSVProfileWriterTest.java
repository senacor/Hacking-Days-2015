package com.senacor.hackingdays.serialization.data.writer;

import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.reader.ProfileReader;
import com.senacor.hackingdays.serialization.data.reader.XMLProfileReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

public class CSVProfileWriterTest {
    public static final int COUNT = 100_000_000;  // -> 7GB!

    @Rule
    public final TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void createCSVData() throws IOException {

        File file = new File("./data.csv"); // tmpFolder.newFile();
        try (ProfileWriter writer = new CSVProfileWriter(file)) {
            ProfileGenerator.newInstance(COUNT).forEach(writer::write);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}