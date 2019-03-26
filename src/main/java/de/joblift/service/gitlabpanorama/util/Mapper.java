package de.joblift.service.gitlabpanorama.util;

import java.time.Instant;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Default ObjectMapper
 */
public class Mapper {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModules(new Jdk8Module(), new JavaTimeModule());

		SimpleModule module = new SimpleModule();
		module.addDeserializer(Instant.class, new InstantDeserializer());
		mapper.registerModule(module);
	}


	public static ObjectMapper get() {
		return mapper;
	}

}
