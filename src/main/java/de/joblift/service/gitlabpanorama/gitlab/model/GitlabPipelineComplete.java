package de.joblift.service.gitlabpanorama.gitlab.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;

import lombok.Data;


/**
 * Used by /projects/:id/pipelines/:pipeline_id
 */
@Data
public class GitlabPipelineComplete extends GitlabPipelineMinimal {

	@JsonProperty
	private boolean tag;
	@JsonProperty("created_at")
	private Instant created;
	@JsonProperty("updated_at")
	private Instant updated;
	@JsonProperty("started_at")
	private Instant started;
	@JsonProperty("finished_at")
	private Instant finished;

	@JsonProperty
	private Long duration; // seconds

	@JsonProperty
	private GitlabUser user;

	public Pipeline toPipeline() {
		Pipeline result = new Pipeline();
		result.setProject(getProject().toProject());
		result.setUser(getUser() == null ? null : getUser().toUser());

		result.setId(getId());
		result.setStatus(getStatus());
		result.setRef(getRef());
		result.setUrl(getUrl());
		result.setTag(isTag());
		result.setCreated(getCreated());
		result.setUpdated(getUpdated());
		result.setStarted(getStarted());
		result.setFinished(getFinished());

		return result;
	}

}
