package de.joblift.service.gitlabpanorama.gitlab.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.Project;

import lombok.Data;


/**
 * Gitlab Project representation
 */
@Data
public class GitlabProject {

	@JsonProperty
	private Long id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty("web_url")
	private String url;
	@JsonProperty
	private String path;
	@JsonProperty("path_with_namespace")
	private String pathNamespaced;
	@JsonProperty("created_at")
	private Instant created;
	@JsonProperty("last_activity_at")
	private Instant lastActivity;
	@JsonProperty("jobs_enabled")
	private boolean jobsEnabled;
	@JsonProperty("avatar_url")
	private String avatar;

	public Project toProject() {
		Project result = new Project();
		result.setId(getId());
		result.setName(getName());
		result.setDescription(getDescription());
		result.setUrl(getUrl());
		result.setPath(getPath());
		result.setPathNamespaced(getPathNamespaced());
		result.setCreated(getCreated());
		result.setLastActivity(getLastActivity());
		result.setAvatar(getAvatar());
		return result;
	}


	@Override
	public String toString() {
		return getId() + ": " + getName();
	}

}
