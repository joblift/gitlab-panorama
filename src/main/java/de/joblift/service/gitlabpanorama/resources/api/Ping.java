package de.joblift.service.gitlabpanorama.resources.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/** Used for healthcheck probes. */
@RestController
@RequestMapping("/ping")
public class Ping {

	@RequestMapping
	public String refresh() {
		return "pong";
	}

}
