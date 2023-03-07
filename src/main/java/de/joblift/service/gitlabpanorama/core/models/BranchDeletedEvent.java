package de.joblift.service.gitlabpanorama.core.models;

import lombok.Value;


/**
 * "Branch has been deleted"-Event for the eventbus.
 */
@Value
public class BranchDeletedEvent {

	private String pathNamespaced;
	private String ref;

}
