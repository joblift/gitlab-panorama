package de.joblift.service.gitlabpanorama.initialization;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


/**
 * Default initialization configuration during startup
 */
@Configuration
@Validated
@ConfigurationProperties(prefix = "init")
public class InitializationConfiguration {

	private boolean collectFromGitlab = true;
	private boolean loadFromStorage = false;


	public boolean isCollectFromGitlab() {
		return collectFromGitlab;
	}


	public void setCollectFromGitlab(boolean collectFromGitlab) {
		this.collectFromGitlab = collectFromGitlab;
	}


	public boolean isLoadFromStorage() {
		return loadFromStorage;
	}


	public void setLoadFromStorage(boolean loadFromStorage) {
		this.loadFromStorage = loadFromStorage;
	}

}
