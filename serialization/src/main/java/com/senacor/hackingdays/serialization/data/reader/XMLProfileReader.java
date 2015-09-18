package com.senacor.hackingdays.serialization.data.reader;

import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Gender;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.Seeking;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class XMLProfileReader implements Closeable, ProfileReader, Iterable<Profile>, Iterator<Profile> {

    private final XMLStreamReader reader;
    private final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
    private boolean eof;

    public XMLProfileReader(String file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public XMLProfileReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public XMLProfileReader(InputStream is) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLStreamReader(is);
            advanceToNextElement();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void advanceToNextElement() throws XMLStreamException {
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.END_DOCUMENT) {
                eof = true;
                return;
            }
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                if (reader.getLocalName().equals(Profile.class.getSimpleName())) {
                    return;
                }
            }
        }
    }

    public Profile next() {
        if (!hasNext())
            throw new NoSuchElementException();

        try {
            Profile profile = createProfile();
            advanceToNextElement();
            return profile;
        } catch (XMLStreamException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Profile createProfile() throws XMLStreamException, ParseException {
        String name = $("name");
        String gender = $(Gender.class.getSimpleName());
        Profile profile = new Profile(name, Gender.valueOf(gender));
        String age = $("age");
        profile.setAge(Integer.valueOf(age));
        profile.setSmoker(Boolean.valueOf($("smoker")));

        Location location = new Location($("city"), $("state"), $("zip"));
        profile.setLocation(location);

        String lastLogin = $("lastLogin");
        String loginCount = $("loginCount");
        Activity activity = new Activity(dateFormat.parse(lastLogin), Integer.valueOf(loginCount));
        profile.setActivity(activity);

        String seekingGender = $("Gender");
        String min = $("min");
        String max = $("max");
        Seeking seeking = new Seeking(Gender.valueOf(seekingGender), new Range(Integer.valueOf(min), Integer.valueOf(max)));
        profile.setSeeking(seeking);
        return profile;

    }

    private String $(String elementName) throws XMLStreamException {
        while (reader.hasNext()) {
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                if (reader.getLocalName().equals(elementName)) {
                    reader.next();
                    return reader.getText();
                }
            }
            reader.next();
        }
        throw new AssertionError();
    }

    public boolean hasNext() {
        return !eof;
    }

    @Override
    public void close() throws IOException {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Profile> iterator() {
        return this;
    }

    @Override
    public Stream<Profile> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
