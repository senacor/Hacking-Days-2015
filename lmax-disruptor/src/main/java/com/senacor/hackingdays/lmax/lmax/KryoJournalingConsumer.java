package com.senacor.hackingdays.lmax.lmax;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.io.Closeables;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.kryo.KryoProfileSerializer;

public class KryoJournalingConsumer extends CompletableConsumer {

	private final KryoProfileSerializer serializer = new KryoProfileSerializer();
	private final OutputStream out;


	public KryoJournalingConsumer(int expectedMessages, Runnable onComplete, OutputStream out) {
		super(expectedMessages, onComplete);
		this.out = out;
	}

	@Override
	protected void processEvent(Profile profile, long sequence, boolean endOfBatch) {
		try {
			out.write(serializer.toBinary(profile));
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	protected void onComplete() {
		try {
			Closeables.close(out, true);
		} catch (IOException e) {
			// not possible
		}
	}

}
