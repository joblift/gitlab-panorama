package de.joblift.service.gitlabpanorama.gitlab;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * Configuration for the Gitlab client
 */
@Validated
@Data
@ConfigurationProperties(prefix = "gitlab")
public class GitlabConfiguration {

	@NotNull
	@NotEmpty
	private String token;

	@NotEmpty
	@URL
	private String endpoint;

	@NotNull
	@NotEmpty
	private String timeout;

}
