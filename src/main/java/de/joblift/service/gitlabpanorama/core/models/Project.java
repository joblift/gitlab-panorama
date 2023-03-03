package de.joblift.service.gitlabpanorama.core.models;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Internal project representation
 */
@Data
@NoArgsConstructor
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

}
