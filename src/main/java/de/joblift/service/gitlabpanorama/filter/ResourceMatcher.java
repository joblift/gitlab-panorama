package de.joblift.service.gitlabpanorama.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Checks if the given entity (project, ref) is matching the specified filter configuration.
 */
@Component
public class ResourceMatcher {

	@Autowired
	FilterConfiguration configuration = new FilterConfiguration();


	public boolean isProjectMatching(String project) {
		return matches(project, configuration.getProjects());
	}


	public boolean isRefMatching(String ref) {
		return matches(ref, configuration.getRefs());
	}


	private boolean matches(String input, FilterLists filter) {
		boolean matchWhitelist = filter.getWhitelistPattern().stream().anyMatch(pattern -> pattern.matcher(input).matches());
		boolean matchBlacklist = filter.getBlacklistPattern().stream().anyMatch(pattern -> pattern.matcher(input).matches());
		return matchWhitelist && !matchBlacklist;
	}

}
