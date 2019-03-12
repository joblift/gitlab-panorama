package de.joblift.service.gitlabpanorama.resources.webhook.models;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.gitlab.model.GitlabProject;


/**
 * Something has been pushed to the repository Webhook. We are ony interested in the deletions of branches to update our
 * internal state. https://docs.gitlab.com/ce/user/project/integrations/webhooks.html#push-events
 */
public class WebhookEventPush extends WebhookEvent {

	private static final String SHA_DELETE = "0000000000000000000000000000000000000000";

	@JsonProperty("event_name")
	private String eventName;
	private String after;
	private String ref;
	@JsonProperty("project")
	private GitlabProject project;


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getAfter() {
		return after;
	}


	public void setAfter(String after) {
		this.after = after;
	}


	public String getRef() {
		return ref;
	}


	public void setRef(String ref) {
		this.ref = ref;
	}


	public GitlabProject getProject() {
		return project;
	}


	public void setProject(GitlabProject project) {
		this.project = project;
	}


	public boolean isBranchRemoved() {
		return StringUtils.equals(after, SHA_DELETE);
	}


	public String getRefRaw() {
		return StringUtils.substringAfter(ref, "refs/heads/");
	}

}
