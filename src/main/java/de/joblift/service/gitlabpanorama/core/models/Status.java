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
	//TODO manual missing
	// WARN  [org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver] Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `de.joblift.service.gitlabpanorama.core.models.Status` from String "manual": not one of the values accepted for Enum class: [skipped, success, pending, canceled, failed, running]]

}
