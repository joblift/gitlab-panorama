package de.joblift.service.gitlabpanorama.core.models;

import java.time.Instant;


/**
 * Internal project representation
 */
public class Project {

	private Long id;
	private String name;
	private String description;
	private String url;
	private String path;
	private String pathNamespaced;
	private Instant created;
	private Instant lastActivity;
	private String avatar;


	@Override
	public String toString() {
		return getId() + "/" + getPathNamespaced();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getPathNamespaced() {
		return pathNamespaced;
	}


	public void setPathNamespaced(String pathNamespaced) {
		this.pathNamespaced = pathNamespaced;
	}


	public Instant getCreated() {
		return created;
	}


	public void setCreated(Instant created) {
		this.created = created;
	}


	public Instant getLastActivity() {
		return lastActivity;
	}


	public void setLastActivity(Instant lastActivity) {
		this.lastActivity = lastActivity;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
