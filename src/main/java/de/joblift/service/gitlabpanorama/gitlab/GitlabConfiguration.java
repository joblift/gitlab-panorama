package de.joblift.service.gitlabpanorama.gitlab;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


/**
 * Configuration for the Gitlab client
 */
@Configuration
@Validated
@ConfigurationProperties(prefix = "gitlab")
public class GitlabConfiguration {

	@NotNull
	@NotEmpty
	private String token;
	@NotEmpty
	@URL
	private String endpoint = "https://gitlab.com/api/v4";
	@NotEmpty
	private String timeout = "20s";


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getEndpoint() {
		return endpoint;
	}


	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}


	public String getTimeout() {
		return timeout;
	}


	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

}
