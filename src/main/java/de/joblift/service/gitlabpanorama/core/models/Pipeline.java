package de.joblift.service.gitlabpanorama.core.models;

import static de.galan.commons.time.Times.*;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Internal pipeline representation
 */
@Data
@NoArgsConstructor
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


	public boolean isFinished() {
		return finished == null;
	}

}
