package de.joblift.service.gitlabpanorama.resources.webhook;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


/**
 * Configuration for the Gitlab Webhook
 */
@Configuration
@Validated
@ConfigurationProperties(prefix = "webhook")
public class WebhookConfiguration {

	private String secretToken;


	public String getSecretToken() {
		return secretToken;
	}


	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

}
