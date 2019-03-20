package de.joblift.service.gitlabpanorama.core.aggregator;

import static de.galan.commons.util.Sugar.*;
import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.models.Status;


/**
 * Groups Pipelines for a ref by status
 */
public class PipelinePair {

	String name;
	String ref;

	Pipeline active;
	Pipeline current;


	public PipelinePair(Pipeline... pipelines) {
		name = pipelines[0].getProject().getName();
		ref = pipelines[0].getRef();

		Comparator<Pipeline> comparatorId = Comparator.comparing(Pipeline::getId);
		Comparator<Pipeline> comparatorIdAndLc = comparatorId.thenComparing(Pipeline::latestChange).reversed();

		List<Pipeline> list = Arrays.asList(pipelines).stream().sorted(comparatorIdAndLc).collect(toList());
		active = list.stream().filter(Pipeline::hasActivity).findFirst().orElse(null);
		current = list.stream().filter(not(Pipeline::hasActivity)).findFirst().orElse(null);

		if ((active != null && current != null) && (current.getId() > active.getId() || current.latestChange().isAfter(active.latestChange()))) {
			active = null;
		}
	}


	public String getName() {
		return name;
	}


	public String getRef() {
		return ref;
	}


	public Pipeline getCurrent() {
		return current;
	}


	public boolean isRunning() {
		return active != null && active.hasActivity();
	}


	public Status getStatus() {
		return ((current != null) ? current : active).getStatus();
	}


	public Pipeline getActive() {
		return active;
	}


	@Override
	public String toString() {
		return getName() + ":" + getRef();
	}

}
