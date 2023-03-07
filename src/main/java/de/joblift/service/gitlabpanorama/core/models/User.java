package de.joblift.service.gitlabpanorama.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Internal user representation
 */
@Data
@NoArgsConstructor
public class User {

	private String name;
	private String username;
	private Long id;
	private String avatar;

	@Override
	public String toString() {
		return getId() + "/" + getUsername();
	}

}
