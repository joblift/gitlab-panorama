package de.joblift.service.gitlabpanorama.core.models;

/**
 * Available states in Gitlab
 */
public enum Status {

	// active
	running,
	pending,

	// sleeping
	success,
	failed,
	canceled,
	skipped;

}
