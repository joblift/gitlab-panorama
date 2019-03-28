package de.joblift.service.gitlabpanorama.util;

import static de.galan.commons.time.Instants.*;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


/**
 * Serilizes Instants a standarized way (currently only used by the storage file).
 */
public class InstantSerializer extends JsonSerializer<Instant> {

	@Override
	public void serialize(Instant value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(from(value).toStringUtc());
	}


	@Override
	public Class<Instant> handledType() {
		return Instant.class;
	}

}
