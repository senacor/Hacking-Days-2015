package com.senacor.hackingdays.distributedcache.serializer;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.senacor.hackingdays.distributedcache.generate.ProfileGenerator;
import com.senacor.hackingdays.distributedcache.generate.model.Activity;
import com.senacor.hackingdays.distributedcache.generate.model.Location;
import com.senacor.hackingdays.distributedcache.generate.model.Profile;
import com.senacor.hackingdays.distributedcache.generate.model.Seeking;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andreas Keefer
 */
public class KryoProfileStreamSerializerTest {

    @Test
    public void testWriteRead() throws Exception {
        KryoProfileStreamSerializer serializer = new KryoProfileStreamSerializer();
        Profile input = ProfileGenerator.newProfile();
        System.out.println("input=" + input);
        TestOutput out = new TestOutput();
        serializer.write(out, input);
        byte[] bytes = out.toByteArray();
        System.out.println("writen bytes: " + bytes.length);
        Profile output = serializer.read(new TestInput(bytes));
        System.out.println("output=" + output);

        assertNotNull(output);
        Activity activity = output.getActivity();
        assertNotNull(activity);
        Location location = output.getLocation();
        assertNotNull(location);
        Seeking seeking = output.getSeeking();
        assertNotNull(seeking);
        assertNotNull(output.getName());

        assertEquals(input.getActivity().getLastLogin(), activity.getLastLogin());
        assertEquals(input.getActivity().getLoginCount(), activity.getLoginCount());
        assertEquals(input.getAge(), output.getAge());
        assertEquals(input.getGender(), output.getGender());
        assertEquals(input.getLocation().getCity(), location.getCity());
        assertEquals(input.getLocation().getState(), location.getState());
        assertEquals(input.getLocation().getZip(), location.getZip());
        assertEquals(input.getName(), output.getName());
        assertEquals(input.getRelationShip(), output.getRelationShip());
        assertEquals(input.getSeeking().getAgeRange().getLower(), seeking.getAgeRange().getLower());
        assertEquals(input.getSeeking().getAgeRange().getUpper(), seeking.getAgeRange().getUpper());
        assertEquals(input.getSeeking().getGender(), seeking.getGender());
    }

    class TestInput extends ByteArrayInputStream implements ObjectDataInput {

        public TestInput(byte[] buf) {
            super(buf);
        }

        @Override
        public byte[] readByteArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public char[] readCharArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int[] readIntArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long[] readLongArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] readDoubleArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public float[] readFloatArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public short[] readShortArray() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T readObject() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Data readData() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public ClassLoader getClassLoader() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ByteOrder getByteOrder() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void readFully(byte[] b) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void readFully(byte[] b, int off, int len) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int skipBytes(int n) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean readBoolean() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public byte readByte() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int readUnsignedByte() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public short readShort() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int readUnsignedShort() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public char readChar() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int readInt() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long readLong() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public float readFloat() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public double readDouble() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String readLine() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String readUTF() throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    class TestOutput extends ByteArrayOutputStream implements ObjectDataOutput {

        @Override
        public void writeByteArray(byte[] bytes) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeCharArray(char[] chars) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeIntArray(int[] ints) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeLongArray(long[] longs) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeDoubleArray(double[] values) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeFloatArray(float[] values) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeShortArray(short[] values) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeObject(Object object) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeData(Data data) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public ByteOrder getByteOrder() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeBoolean(boolean v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeByte(int v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeShort(int v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeChar(int v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeInt(int v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeLong(long v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeFloat(float v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeDouble(double v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeBytes(String s) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeChars(String s) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeUTF(String s) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}