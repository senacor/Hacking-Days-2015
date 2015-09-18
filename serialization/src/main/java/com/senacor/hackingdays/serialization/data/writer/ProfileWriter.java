package com.senacor.hackingdays.serialization.data.writer;

import com.senacor.hackingdays.serialization.thirftdata.Profile;

public interface ProfileWriter extends AutoCloseable {
    void write(Profile profile);
}
