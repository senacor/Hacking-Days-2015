package com.senacor.hackingdays.lmax.lmax;

import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.rule.EmbeddedMongoRule;

public class MongoJournalingDisruptorConsumerTest {
	private static final String COLLECTION_NAME_PROFILES = "profiles";

	@ClassRule
	public static final EmbeddedMongoRule embeddedMongoRule = new EmbeddedMongoRule("localhost", 65000);

	private static MongoClient mongoClient;

	private static MongoCollection<Document> profilesCollection;

	@BeforeClass
	public static void setupClass() throws Exception {
		mongoClient = embeddedMongoRule.getClient();

		final MongoDatabase database = mongoClient.getDatabase("MongoJournalingDisruptorConsumerTest");
		database.createCollection(COLLECTION_NAME_PROFILES);;
		profilesCollection = database.getCollection(COLLECTION_NAME_PROFILES, Document.class);
	}


	@Test
	public void testName() throws Exception {
		final int sampleSize = 20;

		final MongoJournalingDisruptorConsumer consumer = new MongoJournalingDisruptorConsumer(sampleSize, () -> {}, profilesCollection, 10);

		final ProfileGenerator profileGenerator = ProfileGenerator.newInstance(sampleSize);

		int sequence = 0;
//		profileGenerator.stream().map(DisruptorEnvelope::wrap).forEach(envelope -> consumer.onEvent(envelope, sequence, false));

		for (final Profile profile : profileGenerator) {
			consumer.onEvent(DisruptorEnvelope.wrap(profile), sequence++, false);
		}

	}

}
