package de.joblift.service.gitlabpanorama.gitlab.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.Status;

import lombok.Data;


/**
 * Used by /projects/:id/pipelines
 */
@Data
public class GitlabPipelineMinimal {

	@JsonIgnore
	private GitlabProject project;
	@JsonProperty
	private Long id;
	@JsonProperty
	private Status status;
	@JsonProperty
	private String ref;
	@JsonProperty("finished_at")
	private Instant finishedAt;
	@JsonProperty("web_url")
	private String url;
	@JsonProperty("created_at")
	private Instant createdAt;

}
