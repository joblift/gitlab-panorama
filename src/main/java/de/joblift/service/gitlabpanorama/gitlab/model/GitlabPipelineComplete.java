package de.joblift.service.gitlabpanorama.gitlab.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;


/**
 * Used by /projects/:id/pipelines/:pipeline_id
 */
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
	GitlabUser user;


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


	public boolean isTag() {
		return tag;
	}


	public void setTag(boolean tag) {
		this.tag = tag;
	}


	public Instant getCreated() {
		return created;
	}


	public void setCreated(Instant created) {
		this.created = created;
	}


	public Instant getUpdated() {
		return updated;
	}


	public void setUpdated(Instant updated) {
		this.updated = updated;
	}


	public Instant getStarted() {
		return started;
	}


	public void setStarted(Instant started) {
		this.started = started;
	}


	public Instant getFinished() {
		return finished;
	}


	public void setFinished(Instant finished) {
		this.finished = finished;
	}


	public Long getDuration() {
		return duration;
	}


	public void setDuration(Long duration) {
		this.duration = duration;
	}


	public GitlabUser getUser() {
		return user;
	}


	public void setUser(GitlabUser user) {
		this.user = user;
	}

}
