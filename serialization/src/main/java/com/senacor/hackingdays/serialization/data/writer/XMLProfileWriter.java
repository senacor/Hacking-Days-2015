package com.senacor.hackingdays.serialization.data.writer;

import com.senacor.hackingdays.serialization.data.Activity;
import com.senacor.hackingdays.serialization.data.Profile;
import com.senacor.hackingdays.serialization.data.Location;
import com.senacor.hackingdays.serialization.data.Range;
import com.senacor.hackingdays.serialization.data.Seeking;
import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class XMLProfileWriter implements Closeable, ProfileWriter {

    public static final String ROOT_ELEMENT = "profiles";

    private final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
    private final XMLStreamWriter writer;

    public XMLProfileWriter(String file) throws FileNotFoundException {
        this(new FileOutputStream(file));
    }

    public XMLProfileWriter(File file) throws FileNotFoundException {
        this(new FileOutputStream(file));
    }

    public XMLProfileWriter(OutputStream os) {
        try {
            writer = new IndentingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(os));
            writer.writeStartDocument("utf-8", "1.0");
            writer.writeStartElement(ROOT_ELEMENT);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(Profile profile) {
        try {
            startElement(profile.getClass());
                writeElement("name", profile.getName());
                writeElement(profile.getGender());
                writeElement("age", profile.getAge());
                writeElement("smoker", profile.isSmoker());
                writeElement(profile.getRelationShip());
                    Location location = profile.getLocation();
                    startElement(location.getClass());
                        writeElement("city", location.getCity());
                        writeElement("state", location.getState());
                        writeElement("zip", location.getZip());
                    endElement();
                    Activity activity = profile.getActivity();
                    startElement(activity.getClass());
                        writeElement("lastLogin", dateFormat.format(activity.getLastLogin()));
                        writeElement("loginCount", activity.getLoginCount());
                    endElement();
                    Seeking seeking = profile.getSeeking();
                    startElement(seeking.getClass());
                    writeElement(seeking.getGender());
                        Range ageRange = seeking.getAgeRange();
                            startElement(ageRange.getClass());
                            writeElement("min", ageRange.getLower());
                            writeElement("max", ageRange.getUpper());
                        endElement();
                    endElement();
            endElement();


        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void endElement() throws XMLStreamException {
        writer.writeEndElement();
    }

    private void writeElement(String smoker, boolean bool) throws XMLStreamException {
        writeElement(smoker, "" + bool);
    }

    private void writeElement(Enum<?> en) throws XMLStreamException {
        writeElement(en.getClass(), en.name());
    }

    private void startElement(Class<?> elementClass) throws XMLStreamException {
        writer.writeStartElement(elementClass.getSimpleName());
    }

    private void writeElement(String elementName, int value) throws XMLStreamException {
        writeElement(elementName, "" + value);
    }

    private void writeElement(Class<?> elementClass, String elementText) throws XMLStreamException {
        writeElement(elementClass.getSimpleName(), elementText);
    }

    private void writeElement(String elementName, String elementText) throws XMLStreamException {
        writer.writeStartElement(elementName);
        writer.writeCharacters(elementText);
        writer.writeEndElement();
    }

    @Override
    public void close() throws IOException {
        try {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
}
