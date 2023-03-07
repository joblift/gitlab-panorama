package de.joblift.service.gitlabpanorama.resources.webhook;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.resources.webhook.models.WebhookEvent;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class WebhookResource {

	private WebhookConfiguration configuration;
	private WebhookService service;

	@RequestMapping("/webhook/test")
	public String webhook(@RequestHeader("X-Gitlab-Event") String gitlabEventType,
			@RequestHeader("X-Gitlab-Token") @Nullable String secretToken,
			@RequestBody String event) {
		validateToken(secretToken);
		Say.info("Received message type {},\n {}", gitlabEventType, event);
		return "accepted";
	}


	@RequestMapping("/webhook")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void webhook(@RequestHeader("X-Gitlab-Event") String gitlabEventType,
			@RequestHeader("X-Gitlab-Token") @Nullable String secretToken,
			@RequestBody WebhookEvent event) {
		validateToken(secretToken);
		Say.info("Received webhook {}", gitlabEventType);

		service.process(event);
	}


	private void validateToken(String secretToken) {
		// docs: https://docs.gitlab.com/ce/user/project/integrations/webhooks.html#secret-token
		if (!StringUtils.equals(configuration.getSecretToken(), secretToken)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid access token");
		}
	}

}
