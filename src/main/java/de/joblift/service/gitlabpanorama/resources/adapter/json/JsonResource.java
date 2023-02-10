package de.joblift.service.gitlabpanorama.resources.adapter.json;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.aggregator.Aggregator;
import de.joblift.service.gitlabpanorama.core.aggregator.PipelinePair;
import de.joblift.service.gitlabpanorama.util.Mapper;

import lombok.AllArgsConstructor;


/**
 * Returns the plain aggregated pipeline pairs
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/adapter/json")
public class JsonResource {

	private Aggregator aggregator;

	@RequestMapping(produces = "application/json")
	public String json() {
		Say.info("Requesting {format}", "json");
		List<PipelinePair> pipelines = aggregator.getPipelines();
		String result = pipelines.stream()
			.map(this::json)
			.filter(Predicate.not(Objects::isNull))
			.collect(joining(",\n"));
		return "[\n" + result + "\n]\n";
	}


	private String json(PipelinePair pair) {
		try {
			return Mapper.get().writeValueAsString(pair);
		}
		catch (Exception ex) {
			return null;
		}
	}

}
