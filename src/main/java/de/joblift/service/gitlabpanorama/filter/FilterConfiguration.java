package de.joblift.service.gitlabpanorama.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


/**
 * Blacklist/Whitelist Configuration for the filter
 */
@Configuration
@Validated
@ConfigurationProperties(prefix = "filter")
public class FilterConfiguration {

	private FilterLists projects = new FilterLists();
	private FilterLists refs = new FilterLists();


	public FilterLists getProjects() {
		return projects;
	}


	public void setProjects(FilterLists projects) {
		this.projects = projects;
	}


	public FilterLists getRefs() {
		return refs;
	}


	public void setRefs(FilterLists refs) {
		this.refs = refs;
	}

}
