package com.senacor.hackingdays.serialization.data.writer.util;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class is a pretty print wrapper for XMLStreamWriter.
 *
 * @author <a href="mailto:tonnhofer@lat-lon.de">Oliver Tonnhofer</a>
 * @author last edited by: $Author$
 * @version $Revision$, $Date$
 */
public class IndentingXMLStreamWriter implements XMLStreamWriter {

    private final String indent;

    private final XMLStreamWriter s;

    private int level = 0;

    // if the last write element call was a start, print end element on the same line
    private boolean lastWasStart = false;

    /**
     * Create a new {@link IndentingXMLStreamWriter} instance with default indentation and no whitespace stripping.
     *
     * @param xmlStreamWriter
     */
    public IndentingXMLStreamWriter(XMLStreamWriter xmlStreamWriter) {
        this.s = xmlStreamWriter;
        this.indent = "  ";
    }

    /**
     * Create a new {@link IndentingXMLStreamWriter} instance with the specified indentation and whitespace stripping
     * policy.
     *
     * @param xmlWriter
     * @param indent    the indent string for each indent level, must not be <code>null</code>
     */
    public IndentingXMLStreamWriter(XMLStreamWriter xmlWriter, String indent) {
        this.s = xmlWriter;
        this.indent = indent;
    }

    public void close()
            throws XMLStreamException {
        s.close();
    }

    public void flush()
            throws XMLStreamException {
        s.flush();
    }

    public NamespaceContext getNamespaceContext() {
        return s.getNamespaceContext();
    }

    public String getPrefix(String uri)
            throws XMLStreamException {
        return s.getPrefix(uri);
    }

    public Object getProperty(String name)
            throws IllegalArgumentException {
        return s.getProperty(name);
    }

    public void setDefaultNamespace(String uri)
            throws XMLStreamException {
        s.setDefaultNamespace(uri);
    }

    public void setNamespaceContext(NamespaceContext context)
            throws XMLStreamException {
        s.setNamespaceContext(context);
    }

    public void setPrefix(String prefix, String uri)
            throws XMLStreamException {
        s.setPrefix(prefix, uri);
    }

    public void writeAttribute(String localName, String value)
            throws XMLStreamException {
        s.writeAttribute(localName, value);
    }

    public void writeAttribute(String namespaceURI, String localName, String value)
            throws XMLStreamException {
        s.writeAttribute(namespaceURI, localName, value);
    }

    public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
            throws XMLStreamException {
        s.writeAttribute(prefix, namespaceURI, localName, value);
    }

    public void writeCData(String data)
            throws XMLStreamException {
        s.writeCData(data);
    }

    public void writeCharacters(String text)
            throws XMLStreamException {
        s.writeCharacters(text);
    }

    public void writeCharacters(char[] text, int start, int len)
            throws XMLStreamException {
        s.writeCharacters(text, start, len);
    }

    public void writeComment(String data)
            throws XMLStreamException {
        indent();
        s.writeComment(data);
        unindent();
    }

    public void writeDTD(String dtd)
            throws XMLStreamException {
        s.writeDTD(dtd);
    }

    public void writeDefaultNamespace(String namespaceURI)
            throws XMLStreamException {
        s.writeDefaultNamespace(namespaceURI);
    }

    public void writeEmptyElement(String localName)
            throws XMLStreamException {
        indent();
        s.writeEmptyElement(localName);
        unindent();
    }

    public void writeEmptyElement(String namespaceURI, String localName)
            throws XMLStreamException {
        indent();
        s.writeEmptyElement(namespaceURI, localName);
        unindent();
    }

    public void writeEmptyElement(String prefix, String localName, String namespaceURI)
            throws XMLStreamException {
        indent();
        s.writeEmptyElement(prefix, localName, namespaceURI);
        unindent();
    }

    public void writeEntityRef(String name)
            throws XMLStreamException {
        s.writeEntityRef(name);
    }

    public void writeNamespace(String prefix, String namespaceURI)
            throws XMLStreamException {
        s.writeNamespace(prefix, namespaceURI);
    }

    public void writeProcessingInstruction(String target)
            throws XMLStreamException {
        s.writeProcessingInstruction(target);
    }

    public void writeProcessingInstruction(String target, String data)
            throws XMLStreamException {
        s.writeProcessingInstruction(target, data);
    }

    public void writeStartDocument()
            throws XMLStreamException {
        s.writeStartDocument();
        s.writeCharacters("\n");
    }

    public void writeStartDocument(String version)
            throws XMLStreamException {
        s.writeStartDocument(version);
        s.writeCharacters("\n");
    }

    public void writeStartDocument(String encoding, String version)
            throws XMLStreamException {
        s.writeStartDocument(encoding, version);
        s.writeCharacters("\n");
    }

    public void writeStartElement(String localName)
            throws XMLStreamException {
        indent();
        s.writeStartElement(localName);
    }

    public void writeStartElement(String namespaceURI, String localName)
            throws XMLStreamException {
        indent();
        s.writeStartElement(namespaceURI, localName);
    }

    public void writeStartElement(String prefix, String localName, String namespaceURI)
            throws XMLStreamException {
        indent();
        s.writeStartElement(prefix, localName, namespaceURI);
    }

    public void writeEndDocument()
            throws XMLStreamException {
        s.writeEndDocument();
    }

    public void writeEndElement()
            throws XMLStreamException {
        unindent();
        s.writeEndElement();
    }

    private final void unindent()
            throws XMLStreamException {
        level -= 1;
        if (!lastWasStart) {
            writeIndent(level);
        }
        if (level == 0) {
            s.writeCharacters("\n");
        }
        lastWasStart = false;
    }

    private final void indent()
            throws XMLStreamException {
        lastWasStart = true;
        writeIndent(level);
        level += 1;
    }

    private final void writeIndent(int level)
            throws XMLStreamException {
        if (level > 0) {
            StringBuilder b = new StringBuilder(level + 1);
            b.append('\n');
            for (int i = 0; i < level; i++) {
                b.append(indent);
            }
            s.writeCharacters(b.toString());
        }
    }
}
