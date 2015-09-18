package com.senacor.hackingdays.serialization.data.generate.xml;

import com.senacor.hackingdays.serialization.data.generate.ProfileGenerator;
import com.senacor.hackingdays.serialization.data.writer.XMLProfileWriter;

import java.io.File;
import java.io.IOException;

public class PersonDatabaseGenerator {


    public static void main(String[] args) throws IOException {


        try (XMLProfileWriter writer = new XMLProfileWriter(new File("src/main/resources/database.xml"))) {
            ProfileGenerator generator = ProfileGenerator.newInstance(1_000_000);
            generator.stream().forEach(writer::write);

        }
    }
}
