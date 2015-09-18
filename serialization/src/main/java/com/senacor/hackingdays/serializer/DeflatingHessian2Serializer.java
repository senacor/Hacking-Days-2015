package com.senacor.hackingdays.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.Deflation;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import akka.serialization.JSerializer;

public class DeflatingHessian2Serializer extends JSerializer {
	/** Logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeflatingHessian2Serializer.class);

	@Override
	public int identifier() {
		return 368763241;
	}


	@Override
	public boolean includeManifest() {
		return true;
	}


	@Override
	public byte[] toBinary(Object object) {
		try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

			final Hessian2Output hessian2Output = new Deflation().wrap(new Hessian2Output(bos));

			hessian2Output.startMessage();
			hessian2Output.writeObject(object);
			hessian2Output.completeMessage();
			hessian2Output.close();

			return bos.toByteArray();

		} catch (IOException e) {
			LOGGER.error("Got exception during serialization", e);
			return null;

		} finally {

		}
	}


	@Override
	public Object fromBinaryJava(byte[] bytes, Class<?> manifest) {
		try (final ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {

			final Hessian2Input hessian2Input = new Deflation().unwrap(new Hessian2Input(bis));

			hessian2Input.startMessage();
			final Object object = hessian2Input.readObject(manifest);
			hessian2Input.completeMessage();
			hessian2Input.close();

			return object;

		} catch (IOException e) {
			LOGGER.error("Got exception during serialization", e);
			return null;
		}
	}

}
