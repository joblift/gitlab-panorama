package de.joblift.service.gitlabpanorama.resources.webhook;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;


/**
 * Configuration for the Gitlab Webhook
 */
@Validated
@Data
@ConfigurationProperties(prefix = "webhook")
public class WebhookConfiguration {

	private String secretToken;

}
