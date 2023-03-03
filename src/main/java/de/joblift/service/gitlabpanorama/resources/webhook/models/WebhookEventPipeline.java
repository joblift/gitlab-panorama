package de.joblift.service.gitlabpanorama.resources.webhook.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineComplete;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabProject;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Pipeline Webhook event from Gitlab.
 * https://docs.gitlab.com/ce/user/project/integrations/webhooks.html#pipeline-events
 */
@Data
@NoArgsConstructor
public class WebhookEventPipeline extends WebhookEvent {

	@JsonProperty("object_attributes")
	private GitlabPipelineComplete attributes;
	@JsonProperty("project")
	private GitlabProject project;

}
