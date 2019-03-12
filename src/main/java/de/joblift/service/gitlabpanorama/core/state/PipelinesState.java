package de.joblift.service.gitlabpanorama.core.state;

import static de.galan.commons.time.Times.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.models.BranchDeletedEvent;
import de.joblift.service.gitlabpanorama.core.models.CleanStateEvent;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;


/**
 * Current representation of the pipelines in gitlab
 */
@Component
public class PipelinesState {

	private Map<String, Pipeline> pipelines = new HashMap<>();


	@Subscribe
	public void onClean(CleanStateEvent event) {
		Say.info("Received CleanState event");
		synchronized (pipelines) {
			pipelines.clear();
		}
	}


	@Subscribe
	public void onPipeline(Pipeline pipeline) {
		Say.info("Received pipeline: {}", pipeline);
		put(pipeline);
	}


	@Subscribe
	public void onBranchDeleted(BranchDeletedEvent event) {
		synchronized (pipelines) {
			Say.info("Event for closed MR - project: {}, branch: {}", event.getPathNamespaced(), event.getRef());
			List<String> keys = pipelines.values().stream()
				.filter(p -> StringUtils.equals(p.getProject().getPathNamespaced(), event.getPathNamespaced()))
				.filter(p -> StringUtils.equals(p.getRef(), event.getRef()))
				.map(this::key)
				.collect(toList());
			keys.stream().forEach(pipelines::remove);
		}
	}


	private void put(Pipeline pipeline) {
		synchronized (pipelines) {
			Pipeline found = pipelines.get(key(pipeline));
			if (found == null) {
				pipelines.put(key(pipeline), pipeline);
			}
			else if (pipeline.getId().equals(found.getId())) {
				if (when(pipeline.latestChange()).after(found.latestChange())) {
					pipelines.put(key(pipeline), pipeline);
				}
			}
			else if (pipeline.getId() > found.getId()) {
				pipelines.put(key(pipeline), pipeline);
			}
		}
	}


	public List<Pipeline> getPipelines() {
		synchronized (pipelines) {
			return new ArrayList<>(pipelines.values());
		}
	}


	private String key(Pipeline pipeline) {
		return pipeline.getProject().getPathNamespaced() + ":" + pipeline.getRef() + ":" + pipeline.getStatus();
	}

}
