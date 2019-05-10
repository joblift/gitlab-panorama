package de.joblift.service.gitlabpanorama.resources.adapter.ccmenu;

import static de.galan.commons.time.Instants.*;
import static java.nio.charset.StandardCharsets.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.aggregator.Aggregator;
import de.joblift.service.gitlabpanorama.core.aggregator.PipelinePair;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.models.Status;


/**
 * Provides an endpoint for ccmenu-compatible clients.
 */
@RestController
@RequestMapping("/api/adapter/ccmenu")
public class CcmenuResource {

	@Autowired
	Aggregator aggregator;


	@RequestMapping
	public String refresh() {
		Say.info("Requesting {format}", "ccmenu");
		List<PipelinePair> pipelines = aggregator.getPipelines();
		StringBuilder builder = new StringBuilder();
		builder.append("<Projects>\n");
		for (PipelinePair pair : pipelines) {
			builder.append("<Project ");
			builder.append("name=\"" + esc(pair.getName()) + "\" \n");
			builder.append("activity=\"" + (pair.isRunning() ? "Building" : "Sleeping") + "\" \n");
			builder.append("lastBuildStatus=\"" + getLastBuildStatus(pair) + "\" \n");
			builder.append("lastBuildTime=\"" + getLastBuildTime(pair) + "\" \n");
			builder.append("lastBuildLabel=\"" + esc(getLastBuildLabel(pair)) + "\" \n");
			builder.append("webUrl=\"" + esc(getWebUrl(pair)) + "\" \n");
			builder.append("/>\n");
		}
		builder.append("</Projects>\n");
		return builder.toString();
	}


	private String esc(String input) {
		return HtmlUtils.htmlEscape(input, UTF_8.name());
	}


	private String getLastBuildLabel(PipelinePair pair) {
		Pipeline p = pair.getCurrent() != null ? pair.getCurrent() : pair.getActive();
		if (p == null) {
			Say.error("Invalid PipelinePair state");
		}
		else if (p.getProject() == null) {
			Say.error("Invalid PipelinePair project state");
		}
		else {
			return p.getRef();
		}
		return "(unknown)";
	}


	private String getWebUrl(PipelinePair pair) {
		if (pair.isRunning()) {
			return pair.getActive().getUrl();
		}
		if (pair.getCurrent() != null) {
			return pair.getCurrent().getUrl();
		}
		return "http://gitlab.com";
	}


	private String getLastBuildTime(PipelinePair pair) {
		if (pair.getCurrent() == null) {
			return null;
		}
		return from(pair.getCurrent().getCreated()).toStringUtc();
	}


	private String getLastBuildStatus(PipelinePair pair) {
		if (pair.getCurrent() == null) {
			return "Unknown";
		}
		if (pair.getCurrent().getStatus() == Status.success) {
			return "Success";
		}
		if (pair.getCurrent().getStatus() == Status.failed) {
			return "Failure";
		}
		// ccmenu does not reflect skipped or manual, show as success since it is at least not failed
		if (pair.getCurrent().getStatus() == Status.skipped) {
			return "Success";
		}
		if (pair.getCurrent().getStatus() == Status.manual) {
			return "Success";
		}
		return "Unknown";
	}

}
