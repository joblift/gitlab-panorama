package de.joblift.service.gitlabpanorama.core.models;

/**
 * "Branch has been deleted"-Event for the eventbus.
 */
public class BranchDeletedEvent {

	private String pathNamespaced;
	private String ref;


	public BranchDeletedEvent(String pathNamespaced, String ref) {
		this.pathNamespaced = pathNamespaced;
		this.ref = ref;
	}


	public String getPathNamespaced() {
		return pathNamespaced;
	}


	public String getRef() {
		return ref;
	}

}
