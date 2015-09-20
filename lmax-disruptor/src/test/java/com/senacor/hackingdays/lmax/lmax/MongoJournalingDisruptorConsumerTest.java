package com.senacor.hackingdays.lmax.lmax;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.generate.model.Profile;
import com.senacor.hackingdays.lmax.rule.EmbeddedMongoRule;
import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MongoJournalingDisruptorConsumerTest {

    @ClassRule
    public static final EmbeddedMongoRule embeddedMongoRule = new EmbeddedMongoRule("localhost", 65000);

    private static MongoClient mongoClient;

    private static MongoCollection<Document> profilesCollection;

    @BeforeClass
    public static void setupClass() throws Exception {
        mongoClient = embeddedMongoRule.getClient();
        mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);


        final MongoDatabase database = mongoClient.getDatabase("MongoJournalingDisruptorConsumerTest");
        database.createCollection(MongoJournalingDisruptorConsumer.COLLECTION_NAME_PROFILES);
        profilesCollection = database.getCollection(MongoJournalingDisruptorConsumer.COLLECTION_NAME_PROFILES, Document.class);
    }


    @Test
    public void testName() throws Exception {
        final int sampleSize = 200_000;

        final MongoJournalingDisruptorConsumer consumer = new MongoJournalingDisruptorConsumer(sampleSize, () -> {
        }, profilesCollection, 10000);

        final ProfileGenerator profileGenerator = ProfileGenerator.newInstance(sampleSize);

		int sequence = 0;
		final Stopwatch stopwatch = Stopwatch.createStarted();

        for (final Profile profile : profileGenerator) {
            consumer.onEvent(DisruptorEnvelope.wrap(profile), sequence++, false);
        }

		stopwatch.stop();
		System.out.println(String.format("processed %1$s events in %2$s millis", profilesCollection.count(), stopwatch.elapsed(MILLISECONDS)));

		assertThat(profilesCollection.count(), is((long)sampleSize));
	}

}
