package de.joblift.service.gitlabpanorama.filter;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;


/**
 * Checks if the given entity (project, ref) is matching the specified filter configuration.
 */
@Component
@AllArgsConstructor
public class ResourceMatcher {

	private FilterConfiguration configuration;

	public boolean isProjectMatching(String project) {
		return matches(project, configuration.getProjects());
	}


	public boolean isRefMatching(String ref) {
		return matches(ref, configuration.getRefs());
	}


	private boolean matches(String input, FilterLists filter) {
		boolean matchAllowlist = filter.getAllowlistPattern().stream().anyMatch(pattern -> pattern.matcher(input).matches());
		boolean matchBlocklist = filter.getBlocklistPattern().stream().anyMatch(pattern -> pattern.matcher(input).matches());
		return matchAllowlist && !matchBlocklist;
	}

}
