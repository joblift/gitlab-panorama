package de.joblift.service.gitlabpanorama.filter;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;


/**
 * Allowlist/Blocklist configuration.
 */
public class FilterLists {

	private List<String> allowlist = Lists.newArrayList(".*");
	private List<String> blocklist = Lists.newArrayList();
	private List<Pattern> allowlistPattern;
	private List<Pattern> blocklistPattern;


	@Override
	public String toString() {
		return "allowlists: " + allowlist + ", blocklists: " + blocklist;
	}


	public List<String> getAllowlist() {
		return allowlist;
	}


	public void setAllowlist(List<String> Allowlist) {
		this.allowlist = Allowlist;
	}


	public List<Pattern> getAllowlistPattern() {
		if (allowlistPattern == null) {
			allowlistPattern = allowlist.stream().map(Pattern::compile).collect(toList());
		}
		return allowlistPattern;
	}


	public List<String> getBlocklist() {
		return blocklist;
	}


	public void setBlocklist(List<String> Blocklist) {
		this.blocklist = Blocklist;
	}


	public List<Pattern> getBlocklistPattern() {
		if (blocklistPattern == null) {
			blocklistPattern = blocklist.stream().map(Pattern::compile).collect(toList());
		}
		return blocklistPattern;
	}

}
