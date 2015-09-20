package com.senacor.hackingdays.lmax.lmax;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.of;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;
import com.mongodb.client.MongoCollection;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class MongoJournalingDisruptorConsumer extends CompletableConsumer implements EventHandler<DisruptorEnvelope>, LifecycleAware {

	public static final String COLLECTION_NAME_PROFILES = "profiles";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private final MongoCollection<Document> profilesCollection;
	private final Profile[] buffer;
	private final int batchSize;
	private final int lastIndex;
	private int index;

	public MongoJournalingDisruptorConsumer(int expectedMessages, Runnable onComplete, MongoCollection<Document> mongoCollection, final int batchSize) {
		super(expectedMessages, onComplete);

		this.profilesCollection = mongoCollection;

		this.batchSize = batchSize;
		this.lastIndex = batchSize - 1;

		this.buffer = new Profile[batchSize];
	}

	@Override
	public void processEvent(Profile profile, long sequence, boolean endOfBatch) {
		index = (int) (sequence % batchSize);

		buffer[(int) index] = profile;

		if (index == lastIndex) {
			profilesCollection.insertMany(toDocuments(buffer));
		}
	}

	@Override
	protected void onComplete() {
		if (index != lastIndex) {
			profilesCollection.insertMany(range(0, index + 1).mapToObj(i -> toDocument(buffer[i])).collect(toList()));
		}

		System.out.println("saved " + profilesCollection.count() + " profiles to mongo journal");
//		for (final Document profile : profilesCollection.find()) {
//			System.out.println(profile);
//		}
	}

	@Override
	public void onShutdown() {
		onComplete();
	}

	@Override
	public void onStart() {
	}


	private static List<Document> toDocuments(Profile... profiles) {
		return of(profiles)
			.map(profile -> toDocument(profile))
			.collect(Collectors.toList());
	}

	private static Document toDocument(Profile profile) {
		try {
			return Document.parse(OBJECT_MAPPER.writeValueAsString(profile));

		} catch (IOException e) {}

		return null;
	}

	private static Profile fromDocument(Document document) {
		try {
			return OBJECT_MAPPER.readValue(document.toJson(), Profile.class);

		} catch (IOException e) {}

		return null;
	}

}
