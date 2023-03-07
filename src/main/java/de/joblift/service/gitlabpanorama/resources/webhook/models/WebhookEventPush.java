package de.joblift.service.gitlabpanorama.resources.webhook.models;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.gitlab.model.GitlabProject;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Something has been pushed to the repository Webhook. We are ony interested in the deletions of branches to update our
 * internal state. https://docs.gitlab.com/ce/user/project/integrations/webhooks.html#push-events
 */
@Data
@NoArgsConstructor
public class WebhookEventPush extends WebhookEvent {

	private static final String SHA_DELETE = "0000000000000000000000000000000000000000";

	@JsonProperty("event_name")
	private String eventName;
	private String after;
	private String ref;
	@JsonProperty("project")
	private GitlabProject project;

	public boolean isBranchRemoved() {
		return StringUtils.equals(after, SHA_DELETE);
	}


	public String getRefRaw() {
		return StringUtils.substringAfter(ref, "refs/heads/");
	}

}
