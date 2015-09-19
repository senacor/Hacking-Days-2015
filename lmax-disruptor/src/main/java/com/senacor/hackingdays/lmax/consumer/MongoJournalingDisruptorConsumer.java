package com.senacor.hackingdays.lmax.consumer;

import static java.util.Arrays.asList;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;
import com.mongodb.client.MongoCollection;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.lmax.DisruptorEnvelope;

public class MongoJournalingDisruptorConsumer implements EventHandler<DisruptorEnvelope>, LifecycleAware {

	private final MongoCollection<Profile> profilesCollection;
	private final Profile[] buffer;
	private final int batchSize;
	private int lastIndex;

	public MongoJournalingDisruptorConsumer(MongoCollection<Profile> mongoCollection, final int batchSize) {
		this.profilesCollection = mongoCollection;

		this.batchSize = batchSize;
		this.lastIndex = batchSize - 1;

		this.buffer = new Profile[batchSize];
	}

	@Override
	public void onEvent(DisruptorEnvelope event, long sequence, boolean endOfBatch) throws Exception {
		final long index = sequence % batchSize;

		buffer[(int) index] = event.getProfile();

		if (index == lastIndex) {
			profilesCollection.insertMany(asList(buffer)); // hmm schlau???
		}
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onShutdown() {
		System.out.println("saved " + profilesCollection.count() + " profiles:");
		for (final Profile profile : profilesCollection.find()) {
			System.out.println(profile);
		}
	}

}
