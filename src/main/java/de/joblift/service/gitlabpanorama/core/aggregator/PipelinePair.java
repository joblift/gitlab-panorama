package de.joblift.service.gitlabpanorama.core.aggregator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.models.Status;

import lombok.Getter;


/**
 * Groups Pipelines for a ref by status
 */
@Getter
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

		List<Pipeline> list = Arrays.stream(pipelines).sorted(comparatorIdAndLc).toList();
		active = list.stream().filter(Pipeline::hasActivity).findFirst().orElse(null);
		current = list.stream().filter(Predicate.not(Pipeline::hasActivity)).findFirst().orElse(null);

		if ((active != null && current != null) && (current.getId() > active.getId() || current.latestChange().isAfter(active.latestChange()))) {
			active = null;
		}
	}


	public boolean isRunning() {
		return active != null && active.hasActivity();
	}


	public Status getStatus() {
		return ((current != null) ? current : active).getStatus();
	}


	@Override
	public String toString() {
		return getName() + ":" + getRef();
	}

}
