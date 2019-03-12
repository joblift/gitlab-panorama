package de.joblift.service.gitlabpanorama.resources.webhook.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineComplete;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabProject;


/**
 * Pipeline Webhook event from Gitlab.
 * https://docs.gitlab.com/ce/user/project/integrations/webhooks.html#pipeline-events
 */
public class WebhookEventPipeline extends WebhookEvent {

	@JsonProperty("object_attributes")
	private GitlabPipelineComplete attributes;
	@JsonProperty("project")
	private GitlabProject project;


	public GitlabPipelineComplete getAttributes() {
		return attributes;
	}


	public void setAttributes(GitlabPipelineComplete attributes) {
		this.attributes = attributes;
	}


	public GitlabProject getProject() {
		return project;
	}


	public void setProject(GitlabProject project) {
		this.project = project;
	}

}
