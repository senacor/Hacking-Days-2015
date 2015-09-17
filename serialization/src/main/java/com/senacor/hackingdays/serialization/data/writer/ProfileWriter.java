package com.senacor.hackingdays.serialization.data.writer;

import com.senacor.hackingdays.serialization.data.Profile;

public interface ProfileWriter extends AutoCloseable {
    void write(Profile profile);
}
