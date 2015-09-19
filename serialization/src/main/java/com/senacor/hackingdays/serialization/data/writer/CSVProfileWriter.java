package com.senacor.hackingdays.serialization.data.writer;

import com.senacor.hackingdays.serialization.data.Profile;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CSVProfileWriter implements Closeable, ProfileWriter {
    private final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
    private final OutputStreamWriter writer;

    public CSVProfileWriter(String file) throws FileNotFoundException {
        this(new FileOutputStream(file));
    }

    public CSVProfileWriter(File file) throws FileNotFoundException {
        this(new FileOutputStream(file));
    }

    public CSVProfileWriter(OutputStream os) {
        writer = new OutputStreamWriter(os);
    }

    @Override
    public void write(Profile profile) {
        StringBuilder sb = new StringBuilder();

        sb.append(profile.getName())
                .append(",")
                .append(profile.getGender().name())
                .append(",")
                .append(profile.getLocation().getZip())
                .append(",")
                .append(profile.getLocation().getCity())
                .append(",")
                .append(profile.getLocation().getState())
                .append(",")
                .append(profile.getRelationShip().name())
                .append(",")
                .append(profile.isSmoker())
                .append(",")
                .append(profile.getSeeking().getGender().name())
                .append(",")
                .append(profile.getSeeking().getAgeRange().getLower())
                .append(",")
                .append(profile.getSeeking().getAgeRange().getUpper())
                .append(",")
                .append(dateFormat.format(profile.getActivity().getLastLogin()))
                .append(",")
                .append(profile.getActivity().getLoginCount())
                .append("\n");
        try {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
