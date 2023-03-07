package de.joblift.service.gitlabpanorama.initialization;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;


/**
 * Default initialization configuration during startup
 */
@Validated
@Data
@ConfigurationProperties(prefix = "init")
public class InitializationConfiguration {

	private boolean collectFromGitlab = true;
	private boolean loadFromStorage = false;

}
