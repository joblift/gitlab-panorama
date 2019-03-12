package de.joblift.service.gitlabpanorama.gitlab.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.Project;


/**
 * Gitlab Project representation
 */
public class GitlabProject {

	@JsonProperty
	Long id;
	@JsonProperty
	String name;
	@JsonProperty
	String description;
	@JsonProperty("web_url")
	String url;
	@JsonProperty
	String path;
	@JsonProperty("path_with_namespace")
	String pathNamespaced;
	@JsonProperty("created_at")
	Instant created;
	@JsonProperty("last_activity_at")
	Instant lastActivity;
	@JsonProperty("jobs_enabled")
	boolean jobsEnabled;


	public void setId(Long id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public void setPathNamespaced(String pathNamespaced) {
		this.pathNamespaced = pathNamespaced;
	}


	public void setCreated(Instant created) {
		this.created = created;
	}


	public void setLastActivity(Instant lastActivity) {
		this.lastActivity = lastActivity;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@JsonProperty("avatar_url")
	String avatar;


	public Long getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}


	public String getUrl() {
		return url;
	}


	public String getPath() {
		return path;
	}


	public String getPathNamespaced() {
		return pathNamespaced;
	}


	public Instant getCreated() {
		return created;
	}


	public Instant getLastActivity() {
		return lastActivity;
	}


	public String getAvatar() {
		return avatar;
	}


	public boolean isJobsEnabled() {
		return jobsEnabled;
	}


	public void setJobsEnabled(boolean jobsEnabled) {
		this.jobsEnabled = jobsEnabled;
	}


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
