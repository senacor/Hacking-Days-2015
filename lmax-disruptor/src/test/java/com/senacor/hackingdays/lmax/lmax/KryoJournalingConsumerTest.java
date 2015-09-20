package com.senacor.hackingdays.lmax.lmax;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.senacor.hackingdays.lmax.generate.ProfileGenerator;
import com.senacor.hackingdays.lmax.generate.model.Profile;

public class KryoJournalingConsumerTest {

	@Test
	public void testName() throws Exception {
		final int sampleSize = 200_000;

		final File outfile = new File("./test.out");
		try (final FileOutputStream out = new FileOutputStream(outfile, false)) {
			final KryoJournalingConsumer consumer = new KryoJournalingConsumer(sampleSize, () -> {}, out);

			final ProfileGenerator profileGenerator = ProfileGenerator.newInstance(sampleSize);

			int sequence = 0;
			final Stopwatch stopwatch = Stopwatch.createStarted();

			for (final Profile profile : profileGenerator) {
				consumer.onEvent(DisruptorEnvelope.wrap(profile), sequence++, false);
			}
			out.flush();
			stopwatch.stop();
			System.out.println(format("processed %1$s events in %2$s millis", sampleSize, stopwatch.elapsed(MILLISECONDS)));
		}

		System.out.println(format("wrote %1$s bytes", outfile.length()));

//		try (final InputStream is = new FileInputStream(outfile)) {
//			while (is.available() > 0) {
//				System.out.print(is.read());
//			}
//		}
	}

}
