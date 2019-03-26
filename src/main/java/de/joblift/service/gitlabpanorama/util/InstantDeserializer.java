package de.joblift.service.gitlabpanorama.util;

import static de.galan.commons.time.Instants.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


/**
 * Due to the fact that GitLab uses different formats for their dates, we catch those here. The API uses
 * "yyyy-MM-dd'T'HH:mm:ss'Z'", the webhooks "yyyy-MM-dd HH:mm:ss 'UTC'", but the tests for webhooks use the same format
 * as the API (d'oh!). Also it seems that some users retrieve the date-format with ISO-8601 offset, eg.
 * 2018-12-19T19:43:57.350+11:00
 */
public class InstantDeserializer extends JsonDeserializer<Instant> {

	private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");


	@Override
	public Instant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Instant result = null;
		String input = jp.getText();
		if (!isBlank(input)) {
			if (input.endsWith("UTC")) {
				result = LocalDateTime.parse(input, DTF).toInstant(ZoneOffset.UTC);
			}
			else if (input.endsWith("Z")) {
				result = instantUtc(input);
			}
			else {
				result = OffsetDateTime.parse(input).toInstant();
			}
		}
		return result;
	}

}
