package de.joblift.service.gitlabpanorama.core.aggregator;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.state.PipelinesState;

import lombok.AllArgsConstructor;


/**
 * Provides a joined view on the project/ref combinations.
 */
@Service
@AllArgsConstructor
public class Aggregator {

	private final PipelinesState state;

	public List<PipelinePair> getPipelines() {
		List<PipelinePair> result = new ArrayList<>();

		List<Pipeline> pipelines = state.getPipelines();
		Multimap<String, Pipeline> map = Multimaps.index(pipelines, this::key);
		for (String key : map.keySet()) {
			result.add(new PipelinePair(map.get(key).toArray(new Pipeline[] {})));
		}
		// Some vizualizer (such as nevergreen) show the elements in the order we provide.
		Comparator<PipelinePair> comparator = comparing(PipelinePair::getName).thenComparing(PipelinePair::getRef);
		//return result.stream().sorted(comparator).peek(pp -> pp.name = StringUtils.substring(DigestUtils.md5Hex(pp.getName()), 0, 10)).collect(toList());
		return result.stream().sorted(comparator).collect(toList());
	}


	private String key(Pipeline pipeline) {
		return pipeline.getProject().getPathNamespaced() + ":" + pipeline.getRef();
	}

}
