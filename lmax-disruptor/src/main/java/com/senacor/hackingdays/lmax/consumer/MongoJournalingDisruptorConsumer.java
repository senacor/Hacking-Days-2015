package com.senacor.hackingdays.lmax.consumer;

import static java.util.Arrays.asList;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;
import com.mongodb.client.MongoCollection;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.CompletableConsumer;
import com.senacor.hackingdays.lmax.lmax.DisruptorEnvelope;

public class MongoJournalingDisruptorConsumer extends CompletableConsumer implements EventHandler<DisruptorEnvelope>, LifecycleAware {

	private final MongoCollection<Profile> profilesCollection;
	private final Profile[] buffer;
	private final int batchSize;
	private int lastIndex;

	public MongoJournalingDisruptorConsumer(int expectedMessages, Runnable onComplete, MongoCollection<Profile> mongoCollection, final int batchSize) {
		super(expectedMessages, onComplete);

		this.profilesCollection = mongoCollection;

		this.batchSize = batchSize;
		this.lastIndex = batchSize - 1;

		this.buffer = new Profile[batchSize];
	}

	@Override
	public void processEvent(Profile profile, long sequence, boolean endOfBatch) {
		final long index = sequence % batchSize;

		buffer[(int) index] = profile;

		if (index == lastIndex) {
			profilesCollection.insertMany(asList(buffer)); // hmm schlau???
		}
	}

	@Override
	protected void onComplete() {
		System.out.println("saved " + profilesCollection.count() + " profiles:");
		for (final Profile profile : profilesCollection.find()) {
			System.out.println(profile);
		}
	}

	@Override
	public void onShutdown() {
		onComplete();
	}

	@Override
	public void onStart() {
	}

}
