package de.joblift.service.gitlabpanorama.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.User;


/**
 * Gitlab user representation
 */
public class GitlabUser {

	@JsonProperty
	private String name;
	@JsonProperty
	private String username;
	@JsonProperty
	private Long id;
	@JsonProperty("avatar_url")
	private String avatar;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public User toUser() {
		User result = new User();
		result.setId(getId());
		result.setName(getName());
		result.setUsername(getUsername());
		result.setAvatar(getAvatar());
		return result;
	}

}
