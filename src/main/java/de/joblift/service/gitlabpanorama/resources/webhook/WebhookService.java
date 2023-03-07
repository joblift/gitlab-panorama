package de.joblift.service.gitlabpanorama.resources.webhook;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;

import de.galan.commons.logging.Say;
import de.galan.commons.util.Retryable;
import de.joblift.service.gitlabpanorama.core.models.BranchDeletedEvent;
import de.joblift.service.gitlabpanorama.gitlab.GitlabClient;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineComplete;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEvent;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEventPipeline;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEventPush;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class WebhookService {

	private EventBus eventbus;
	private GitlabClient client;

	public void process(WebhookEvent event) {
		new Thread(() -> {
			if (event instanceof WebhookEventPipeline) {
				processPipeline((WebhookEventPipeline)event);
			}
			else if (event instanceof WebhookEventPush) {
				processPush((WebhookEventPush)event);
			}
		}).start();
	}


	private void processPipeline(WebhookEventPipeline event) {
		GitlabPipelineComplete attributes = event.getAttributes();
		Say.info("Processing pipeline webhook event id {}, ref {}", attributes.getId(), attributes.getRef());
		try {
			Retryable.retry(3).timeToWait("5s")
				.message("Invalid response for pipeline webhook event id '" + attributes.getId() + "', ref '" + attributes.getRef() + "'")
				.call(() -> {
					GitlabPipelineComplete fetched = client.retrievePipelineComplete(event.getProject(), attributes.getRef(), attributes.getId());
					if (fetched == null) {
						return null;
					}
					// debugging invalid events
					if (fetched.getId() == null) {
						throw new RuntimeException("Invalid response for webhook event");
					}
					eventbus.post(fetched.toPipeline());
					return null;
				});
		}
		catch (Exception ex) {
			Say.error("Unable to receive pipeline for webhook event id {}, ref {}", ex, attributes.getId(), attributes.getRef());
		}
	}


	private void processPush(WebhookEventPush event) {
		if (event.isBranchRemoved()) {
			Say.info("Webhook for closed branch - project: {}, branch: {}", event.getProject().getPathNamespaced(), event.getRefRaw());
			eventbus.post(new BranchDeletedEvent(event.getProject().getPathNamespaced(), event.getRefRaw()));
		}
	}

}
