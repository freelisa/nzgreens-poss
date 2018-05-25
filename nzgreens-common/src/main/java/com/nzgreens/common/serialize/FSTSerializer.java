package com.nzgreens.common.serialize;

import org.nustaq.serialization.FSTConfiguration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author hcf
 * @version V1.0
 */
public class FSTSerializer implements RedisSerializer<Object> {

	static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();

	@Override
	public byte[] serialize(Object o) throws SerializationException {
		if (o == null) {
			return null;
		}
		return configuration.asByteArray(o);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null) {
			return null;
		}
		return configuration.asObject(bytes);
	}
}
