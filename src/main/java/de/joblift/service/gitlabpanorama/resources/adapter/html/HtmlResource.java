package de.joblift.service.gitlabpanorama.resources.adapter.html;

import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.galan.commons.logging.Say;


/**
 * Develivers a website which renders the pipelines
 */
@RestController
@RequestMapping("/api/adapter/html")
public class HtmlResource {

	@RequestMapping
	public ResponseEntity<String> html() throws IOException {
		Say.info("Requesting {format}", "html");
		InputStream stream = getClass().getResourceAsStream("index.html");
		String body = new String(stream.readAllBytes(), UTF_8);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate");

		return new ResponseEntity<>(body, headers, HttpStatus.OK);
	}

}
