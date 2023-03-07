package de.joblift.service.gitlabpanorama.gitlab.model;

import lombok.Data;


/**
 * A gitlab branch. Must be retrieved in order to see if the branch still exists.
 */
@Data
public class GitlabBranch {

	private String name;
	private boolean merged;

}
