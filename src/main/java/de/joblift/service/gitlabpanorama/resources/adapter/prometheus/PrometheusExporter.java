package de.joblift.service.gitlabpanorama.resources.adapter.prometheus;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.aggregator.Aggregator;
import de.joblift.service.gitlabpanorama.core.aggregator.PipelinePair;


/**
 * Metrics adapter for a prometheus scraper
 */
@RestController
@RequestMapping("/api/adapter/prometheus")
public class PrometheusExporter {

	@Autowired
	Aggregator aggregator;


	@RequestMapping
	public String bash() {
		Say.info("Requesting {format}", "prometheus");
		List<PipelinePair> pipelines = aggregator.getPipelines();
		return pipelines.stream()
			.filter(pair -> pair.getCurrent() != null)
			.map(pair -> "gitlab_pipeline_state{repository=\"" + pair.getName() + "\",ref=\"" + pair.getRef() + "\"} " + pair.getCurrent().getStatus())
			.collect(Collectors.joining("\n"));
	}

}
