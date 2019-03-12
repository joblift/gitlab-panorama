package de.joblift.service.gitlabpanorama.storage.local;

import static org.apache.commons.lang3.StringUtils.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.google.common.base.StandardSystemProperty;


/**
 * Local storage configuration
 */
@Configuration
@Validated
@ConfigurationProperties(prefix = "storage")
public class LocalStorageConfiguration {

	String path = StandardSystemProperty.USER_HOME.value() + "/.gitlab-panorama";


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public boolean isEnabled() {
		return isNotBlank(path);
	}

}
