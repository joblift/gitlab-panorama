package de.joblift.service.gitlabpanorama.gitlab.model;

/**
 * A gitlab branch. Must be retrieved in order to see if the branch still exists.
 */
public class GitlabBranch {

	private String name;
	private boolean merged;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isMerged() {
		return merged;
	}


	public void setMerged(boolean merged) {
		this.merged = merged;
	}

}
