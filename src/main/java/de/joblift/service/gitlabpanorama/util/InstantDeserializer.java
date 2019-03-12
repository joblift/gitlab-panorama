package de.joblift.service.gitlabpanorama.util;

import static de.galan.commons.time.Instants.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


/**
 * Due to the fact that GitLab uses different formats for their dates, we catch those here. The API uses
 * "yyyy-MM-dd'T'HH:mm:ss'Z'", the webhooks "yyyy-MM-dd HH:mm:ss 'UTC'", but the tests for webhooks use the same format
 * as the API (d'oh!).
 */
public class InstantDeserializer extends JsonDeserializer<Instant> {

	private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");


	@Override
	public Instant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String input = jp.getText();
		if (isBlank(input)) {
			return null;
		}
		if (input != null && input.endsWith("UTC")) {
			Instant instant = LocalDateTime.parse(input, DTF).toInstant(ZoneOffset.UTC);
			return instant;
		}
		return instantUtc(input);
	}

}
