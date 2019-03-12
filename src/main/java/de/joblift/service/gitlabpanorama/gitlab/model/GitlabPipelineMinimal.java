package de.joblift.service.gitlabpanorama.gitlab.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.Status;


/**
 * Used by /projects/:id/pipelines
 */
public class GitlabPipelineMinimal {

	@JsonIgnore
	GitlabProject project;

	@JsonProperty
	Long id;

	@JsonProperty
	Status status;

	@JsonProperty
	String ref;

	@JsonProperty("finished_at")
	Instant finishedAt;


	public Instant getCreatedAt() {
		return createdAt;
	}


	public Instant getFinishedAt() {
		return finishedAt;
	}

	@JsonProperty("web_url")
	String url;

	@JsonProperty("created_at")
	Instant createdAt;


	public void setId(Long id) {
		this.id = id;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public void setRef(String ref) {
		this.ref = ref;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}


	public void setFinishedAt(Instant finishedAt) {
		this.finishedAt = finishedAt;
	}


	public GitlabProject getProject() {
		return project;
	}


	public void setProject(GitlabProject project) {
		this.project = project;
	}


	public Long getId() {
		return id;
	}


	public Status getStatus() {
		return status;
	}


	public String getRef() {
		return ref;
	}


	public String getUrl() {
		return url;
	}

}
