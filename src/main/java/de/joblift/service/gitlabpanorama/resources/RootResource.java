package de.joblift.service.gitlabpanorama.resources;

import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.galan.commons.logging.Say;


/**
 * Returns a simple website, that returns the available endpoints.
 */
@RestController
public class RootResource {

	@RequestMapping("/")
	public ResponseEntity<String> root(@RequestHeader HttpHeaders headers) throws IOException {
		if (headers.getAccept().contains(MediaType.TEXT_HTML)) {
			Say.info("Requesting {format}", "html");
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate");
			return new ResponseEntity<>(read("index.html"), responseHeaders, HttpStatus.OK);
		}
		Say.info("Requesting {format}", "text");
		return new ResponseEntity<>(read("index.txt"), HttpStatus.OK);
	}


	private String read(String filename) throws IOException {
		InputStream stream = getClass().getResourceAsStream(filename);
		return IOUtils.toString(stream, UTF_8);
	}

}
