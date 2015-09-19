package com.senacor.hackingdays.lmax.rule;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.senacor.hackingdays.lmax.lmax.MongoJournalingDisruptorConsumerTest;

import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongoRule extends ExternalResource {
	/** Logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoJournalingDisruptorConsumerTest.class);

	private MongodForTestsFactory mongodForTestsFactory;
	private final String host;
	private final int port;

	public EmbeddedMongoRule() {
		this(null, -1);
	}

	public EmbeddedMongoRule(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	protected void before() throws Throwable {
		final int port = this.port > 0 ? this.port : Network.getFreeServerPort();
		final String host = this.host != null ? this.host : Network.getLocalHost().getHostName();

		LOGGER.info("starting embedded mongodb on address: {}:{}", host, port);

		mongodForTestsFactory = new MongodForTestsFactory(Version.Main.PRODUCTION) {
			@Override
			protected IMongodConfig newMongodConfig(IFeatureAwareVersion version) throws UnknownHostException, IOException {
				return new MongodConfigBuilder()
				.version(version)
				.net(new Net(host, port, Network.localhostIsIPv6()))
				.build();
			}

//			@Override
//			public MongoClient newMongo() throws UnknownHostException, MongoException {
//				final MongoClient template = super.newMongo();
//				return new MongoClient(
//					template.getAddress(),
//					builder(template.getMongoClientOptions())
//					.codecRegistry(fromRegistries(getDefaultCodecRegistry(), fromCodecs(new DocumentCodec())))
//					.build());
//			}
		};
	}

	@Override
	protected void after() {
		LOGGER.info("stopping embedded mongodb on address: {}:{}", host, port);

		if (mongodForTestsFactory != null) {
			mongodForTestsFactory.shutdown();
		}
	}

	public MongoClient getClient() throws Exception {
		return mongodForTestsFactory.newMongo();
	}

}
