package de.joblift.service.gitlabpanorama.resources.adapter.bashboard;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.aggregator.Aggregator;
import de.joblift.service.gitlabpanorama.core.aggregator.PipelinePair;
import de.joblift.service.gitlabpanorama.core.models.Status;


/**
 * Displays the state for the pipelines as shell-compatible output. Bashboard. Best used with watch, eg:</br>
 * <code>watch --color -t 'curl -s &lt;host&gt;:&lt;port&gt;/api/adapter/shell'</code>
 */
@RestController
@RequestMapping("/api/adapter/shell")
public class ShellResource {

	@Autowired
	Aggregator aggregator;


	@RequestMapping(produces = {"text/plain"})
	public String shell(@RequestParam(defaultValue = "true") Boolean dots,
		@RequestParam(defaultValue = EMPTY) String filterStatus,
		@RequestParam(defaultValue = "true") Boolean refs,
		@RequestParam(defaultValue = "true") Boolean lists,
		@RequestParam(defaultValue = ", ") String delimiterProjects,
		@RequestParam(defaultValue = "\n") String delimiterLists) {
		Say.info("Requesting {format}", "bash");
		List<PipelinePair> pipelines = aggregator.getPipelines();
		StringBuilder builder = new StringBuilder();

		List<Status> filterStatusList = Splitter.on(",").omitEmptyStrings().splitToList(filterStatus).stream().map(Status::valueOf).collect(toList());

		if (dots) {
			for (PipelinePair pair : pipelines) {
				if (matches(pair, filterStatusList)) {
					String icon = pair.isRunning() ? "▶" : "●";
					builder.append(ansi().fg(getLastBuildStatus(pair)))
						.append(icon)
						.append(ansi().fg(Color.DEFAULT));
				}
			}
			builder.append("\n");
		}

		if (lists) {
			String stringFailures = list(pipelines, Status.failed, delimiterProjects, refs, filterStatusList);
			String stringSkipped = list(pipelines, Status.skipped, delimiterProjects, refs, filterStatusList);
			String stringSuccess = list(pipelines, Status.success, delimiterProjects, refs, filterStatusList);
			builder.append(Joiner.on(delimiterLists).skipNulls().join(stringFailures, stringSkipped, stringSuccess));
		}
		return builder.toString();

	}


	private boolean matches(PipelinePair pair, List<Status> filterStatusList) {
		if (!filterStatusList.isEmpty() && pair.getCurrent() != null) {
			return !filterStatusList.contains(pair.getCurrent().getStatus());
		}
		return true;
	}


	private String list(List<PipelinePair> pipelines, Status status, String delimiter, boolean displayRef, List<Status> filterStatusList) {
		String result = pipelines.stream()
			.filter(pair -> pair.getCurrent() != null)
			.filter(pair -> pair.getCurrent().getStatus().equals(status))
			.filter(pair -> matches(pair, filterStatusList))
			.map(pair -> ansi().fg(getLastBuildStatus(pair)) + pair.getName() + (displayRef ? " (" + pair.getRef() + ")" : EMPTY) + ansi().reset())
			.collect(joining(delimiter));
		return StringUtils.isBlank(result) ? null : result;
	}


	private Color getLastBuildStatus(PipelinePair pair) {
		if (pair.getCurrent() == null) {
			return Color.BLUE;
		}

		switch (pair.getCurrent().getStatus()) {
			case success:
				return Color.GREEN;
			case failed:
				return Color.RED;
			case skipped:
				return Color.WHITE;
			default:
				return Color.MAGENTA;
		}

	}

}
