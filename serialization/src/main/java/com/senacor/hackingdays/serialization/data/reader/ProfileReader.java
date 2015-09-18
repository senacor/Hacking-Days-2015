package com.senacor.hackingdays.serialization.data.reader;

import com.senacor.hackingdays.serialization.data.Profile;

import java.io.Closeable;
import java.util.Iterator;
import java.util.stream.Stream;

public interface ProfileReader extends Iterator<Profile>, Closeable {

    Stream<Profile> stream();
}
