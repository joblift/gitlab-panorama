package de.joblift.service.gitlabpanorama.resources.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;

import de.galan.commons.logging.Say;
import de.galan.commons.time.Sleeper;
import de.joblift.service.gitlabpanorama.core.models.BranchDeletedEvent;
import de.joblift.service.gitlabpanorama.gitlab.GitlabClient;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineComplete;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEvent;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEventPipeline;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEventPush;


@Service
public class WebhookService {

	@Autowired
	EventBus eventbus;
	@Autowired
	GitlabClient client;


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
		GitlabPipelineComplete attribute = event.getAttributes();
		// fetch again, since not all fields are available in the webhook object
		Say.info("Processing pipeline webhook event id {}, ref {}", attribute.getId(), attribute.getRef());
		GitlabPipelineComplete fetched = client.retrievePipelineComplete(event.getProject(), attribute.getRef(), attribute.getId());
		// debugging invalid events
		if (fetched.getId() == null) {
			Say.info("Received invalid pipeline id {}, sleeping", fetched.getId());
			Sleeper.sleep("5s");
			fetched = client.retrievePipelineComplete(event.getProject(), attribute.getRef(), attribute.getId());
			Say.info("Newly received pipeline id {}", fetched.getId());
		}
		eventbus.post(fetched.toPipeline());
	}


	private void processPush(WebhookEventPush event) {
		if (event.isBranchRemoved()) {
			Say.info("Webhook for closed branch - project: {}, branch: {}", event.getProject().getPathNamespaced(), event.getRefRaw());
			eventbus.post(new BranchDeletedEvent(event.getProject().getPathNamespaced(), event.getRefRaw()));
		}
	}

}
