package com.senacor.hackingdays.lmax.rule;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.rules.ExternalResource;

import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongoRule extends ExternalResource {

	private MongodForTestsFactory mongodForTestsFactory;
	private final String host;
	private final int port;

	public EmbeddedMongoRule(String host, int port) throws Exception {
		this.host = host;
		this.port = port;
	}

	@Override
	protected void before() throws Throwable {
		mongodForTestsFactory = new MongodForTestsFactory(Version.Main.PRODUCTION) {
			@Override
			protected IMongodConfig newMongodConfig(IFeatureAwareVersion version) throws UnknownHostException, IOException {
				return new MongodConfigBuilder()
				.version(version)
				.net(new Net(host, port, Network.localhostIsIPv6()))
				.build();
			}
		};
	}

	@Override
	protected void after() {
		if (mongodForTestsFactory != null) {
			mongodForTestsFactory.shutdown();
		}
	}

	public MongoClient getClient() {
		return new MongoClient(host, port);
	}

}
