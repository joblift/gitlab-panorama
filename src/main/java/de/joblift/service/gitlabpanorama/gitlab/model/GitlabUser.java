package de.joblift.service.gitlabpanorama.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.core.models.User;

import lombok.Data;


/**
 * Gitlab user representation
 */
@Data
public class GitlabUser {

	@JsonProperty
	private String name;
	@JsonProperty
	private String username;
	@JsonProperty
	private Long id;
	@JsonProperty("avatar_url")
	private String avatar;

	public User toUser() {
		User result = new User();
		result.setId(getId());
		result.setName(getName());
		result.setUsername(getUsername());
		result.setAvatar(getAvatar());
		return result;
	}

}
