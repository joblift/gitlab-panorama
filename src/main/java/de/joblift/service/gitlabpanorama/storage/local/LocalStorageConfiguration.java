package de.joblift.service.gitlabpanorama.storage.local;

import static org.apache.commons.lang3.StringUtils.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.google.common.base.StandardSystemProperty;

import de.galan.commons.util.Sugar;

import lombok.Data;


/**
 * Local storage configuration
 */
@Validated
@Data
@ConfigurationProperties(prefix = "storage")
public class LocalStorageConfiguration {

	private String path;

	public LocalStorageConfiguration(String path) {
		this.path = Sugar.first(path, StandardSystemProperty.USER_HOME.value() + "/.gitlab-panorama");
	}


	public boolean isEnabled() {
		return isNotBlank(path);
	}

}
