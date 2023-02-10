package de.joblift.service.gitlabpanorama.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import de.galan.commons.util.Sugar;

import lombok.Data;


/**
 * Blocklist/Allowlist Configuration for the filter
 */
@Data
@Validated
@ConfigurationProperties(prefix = "filter")
public class FilterConfiguration {

	private FilterLists projects;
	private FilterLists refs;

	public FilterConfiguration(FilterLists projects, FilterLists refs) {
		this.projects = Sugar.first(projects, new FilterLists());
		this.refs = Sugar.first(refs, new FilterLists());
	}

}
