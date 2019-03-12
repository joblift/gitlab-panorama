package de.joblift.service.gitlabpanorama.filter;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;


/**
 * Whitelist/Blacklist configuration.
 */
public class FilterLists {

	private List<String> whitelist = Lists.newArrayList(".*");
	private List<String> blacklist = Lists.newArrayList();
	private List<Pattern> whitelistPattern;
	private List<Pattern> blacklistPattern;


	@Override
	public String toString() {
		return "whitelists: " + whitelist + ", blacklists: " + blacklist;
	}


	public List<String> getWhitelist() {
		return whitelist;
	}


	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}


	public List<Pattern> getWhitelistPattern() {
		if (whitelistPattern == null) {
			whitelistPattern = whitelist.stream().map(Pattern::compile).collect(toList());
		}
		return whitelistPattern;
	}


	public List<String> getBlacklist() {
		return blacklist;
	}


	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}


	public List<Pattern> getBlacklistPattern() {
		if (blacklistPattern == null) {
			blacklistPattern = blacklist.stream().map(Pattern::compile).collect(toList());
		}
		return blacklistPattern;
	}

}
