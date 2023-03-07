package de.joblift.service.gitlabpanorama.resources.webhook.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Getter;
import lombok.Setter;


/** See https://docs.gitlab.com/ee/user/project/integrations/webhooks.html */
@JsonTypeInfo(use = Id.NAME, property = "object_kind", include = As.PROPERTY)
@JsonSubTypes(value = {
	@JsonSubTypes.Type(value = WebhookEventPipeline.class, name = WebhookEvent.TYPE_PIPELINE),
	@JsonSubTypes.Type(value = WebhookEventPush.class, name = WebhookEvent.TYPE_PUSH)
})
@Getter
@Setter
public abstract class WebhookEvent {

	public final static String TYPE_PIPELINE = "pipeline";
	public final static String TYPE_MR = "merge_request";
	public final static String TYPE_PUSH = "push";

	// why is this field not set from jackson?
	@JsonProperty("object_kind")
	private String type;

	@Override
	public String toString() {
		return "Webhook, type: " + getClass().getSimpleName();
	}

}
