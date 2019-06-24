package de.joblift.service.gitlabpanorama.util;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


/**
 * Due to the fact that GitLab uses different formats for their dates, we catch those here. The API uses
 * "yyyy-MM-dd'T'HH:mm:ss'Z'", the webhooks "yyyy-MM-dd HH:mm:ss 'UTC'", but the tests for webhooks use the same format
 * as the API (d'oh!). Also it seems that some users retrieve the date-format with ISO-8601 offset, eg.
 * "2018-12-19T19:43:57.350+11:00" or "2019-06-13 16:36:52 +0200".
 */
public class InstantDeserializer extends JsonDeserializer<Instant> {

	private final static Pattern PATTERN_UTC_1 = Pattern.compile("^\\d{4}-\\d{1,2}[-]\\d{1,2} \\d{2}:\\d{2}:\\d{2} UTC$"); // 2019-06-21 15:49:38 UTC
	private final static Pattern PATTERN_UTC_2 = Pattern.compile("^\\d{4}-\\d{1,2}[-]\\d{1,2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,3})?Z$"); // 2019-06-21T15:49:38Z
	private final static Pattern PATTERN_LOCAL_1 = Pattern.compile("^\\d{4}-\\d{1,2}[-]\\d{1,2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+]\\d{2}:\\d{2}$"); // 2018-12-19T19:43:57.350+11:00
	//private final static Pattern PATTERN_LOCAL_2 = Pattern.compile("^\\d{4}-\\d{1,2}[-]\\d{1,2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} [+]\\d{2}\\d{2}$"); // 2018-12-19T19:43:57.350 +11:00
	private final static DateTimeFormatter DTF_UTC_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
	private final static DateTimeFormatter DTF_UTC_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]'Z'");
	private final static DateTimeFormatter DTF_FALLBACK = DateTimeFormatter.ofPattern(
		"[yyyyMMdd][yyyy-MM-dd][yyyy-DDD][['T'][ ][HHmmss][HHmm][HH:mm:ss][HH:mm][.SSSSSSSSS][.SSSSSS][.SSS][.SS][.S]][ ][OOOO][O][z][XXXXX][XXXX]['['VV']']");


	@Override
	public Instant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Instant result = null;
		String input = jp.getText();
		if (!isBlank(input)) {
			if (PATTERN_UTC_1.matcher(input).matches()) {
				result = LocalDateTime.parse(input, DTF_UTC_1).toInstant(ZoneOffset.UTC);
			}
			else if (PATTERN_UTC_2.matcher(input).matches()) {
				result = LocalDateTime.parse(input, DTF_UTC_2).toInstant(ZoneOffset.UTC);
			}
			else if (PATTERN_LOCAL_1.matcher(input).matches()) {
				result = OffsetDateTime.parse(input).toInstant();
			}
			else { // if (PATTERN_LOCAL_2.matcher(input).matches()) {
				result = OffsetDateTime.parse(input, DTF_FALLBACK).toInstant();
			}
		}
		return result;
	}

}
