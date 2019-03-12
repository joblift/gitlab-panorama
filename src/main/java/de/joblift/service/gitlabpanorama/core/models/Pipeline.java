package de.joblift.service.gitlabpanorama.core.models;

import static de.galan.commons.time.Times.*;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Internal pipeline representation
 */
public class Pipeline {

	private Project project;
	private User user; // can be null, eg. when invoked by API

	private Long id;
	private String url;
	private Status status;
	private String ref;

	private boolean tag;
	private Instant created;
	private Instant updated;
	private Instant started;
	private Instant finished;


	@Override
	public String toString() {
		return getId() + "/" + getProject().getPathNamespaced() + "/" + getRef() + "/" + getStatus();
	}


	@JsonIgnore
	public boolean hasActivity() {
		return status == Status.pending || status == Status.running;
	}


	@JsonIgnore
	public Instant latestChange() {
		Instant result = latest(created, started);
		result = latest(result, updated);
		return latest(result, finished);
	}


	private Instant latest(Instant first, Instant second) {
		if (first == null) {
			return second;
		}
		else if (second == null) {
			return first;
		}
		return when(first).after(second) ? first : second;
	}


	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public String getRef() {
		return ref;
	}


	public void setRef(String ref) {
		this.ref = ref;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
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


	public boolean isFinished() {
		return finished == null;
	}

}
