package de.joblift.service.gitlabpanorama.core.models;

/**
 * Internal user representation
 */
public class User {

	private String name;
	private String username;
	private Long id;
	private String avatar;


	@Override
	public String toString() {
		return getId() + "/" + getUsername();
	}


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

}
