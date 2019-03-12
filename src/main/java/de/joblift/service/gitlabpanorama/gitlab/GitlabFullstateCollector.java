package de.joblift.service.gitlabpanorama.gitlab;

import static java.util.concurrent.TimeUnit.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;
import com.google.common.eventbus.EventBus;

import de.galan.commons.logging.Say;
import de.galan.commons.time.Durations;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.filter.ResourceMatcher;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineComplete;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineMinimal;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabProject;


@Component
public class GitlabFullstateCollector {

	@Autowired
	GitlabClient client;

	@Autowired
	EventBus eventbus;

	@Autowired
	ResourceMatcher filter;


	public void refresh() {
		Stopwatch watch = Stopwatch.createStarted();
		List<GitlabProject> projects = client.retrieveProjects();
		for (GitlabProject project : projects) {
			List<GitlabPipelineMinimal> minimals = client.retrieveProjectPipelineMinimals(project);
			for (GitlabPipelineMinimal minimal : minimals) {
				if (passesFilter(minimal) && isBranchExisting(project, minimal.getRef())) {
					GitlabPipelineComplete complete = client.retrievePipelineComplete(minimal.getProject(), minimal.getRef(), minimal.getId());
					if (complete != null) {
						Pipeline pipeline = complete.toPipeline();
						Say.info("Pushing event to eventbus {}", pipeline);
						eventbus.post(pipeline);
					}
				}
			}
		}
		Say.info("Fullstate-refresh finished, took {} and {} gitlab requests",
			Durations.humanize(watch.stop().elapsed(MILLISECONDS), SPACE), client.getRequestCount());
	}


	private boolean passesFilter(GitlabPipelineMinimal minimal) {
		return minimal.getRef() != null && filter.isRefMatching(minimal.getRef());
	}


	private boolean isBranchExisting(GitlabProject project, String ref) {
		return client.retrieveBranch(project, ref) != null; // tags will be catched here too
	}

}
