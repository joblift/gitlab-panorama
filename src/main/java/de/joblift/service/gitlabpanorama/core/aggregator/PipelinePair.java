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

		/*
		List<Pipeline> collected = Arrays.asList(pipelines).stream()
			.collect(groupingBy(Pipeline::getStatus,
				collectingAndThen(maxBy((p1, p2) ->  p1.getId().compareTo(p2.getId())),
					Optional::get)))
			.values().stream().collect(toList());
		
		for (Pipeline pipeline : collected) {
			if (pipeline.hasActivity()) {
				if (active == null || pipeline.latestChange().isAfter(active.latestChange())) {
					active = pipeline;
				}
			}
			else {
				if (current == null || pipeline.latestChange().isAfter(current.latestChange())) {
					current = pipeline;
				}
			}
		}
		if ((active != null && current != null) && (current.getId() > active.getId() || current.latestChange().isAfter(active.latestChange()))) {
			active = null;
		}
		*/

		/*

		// different algo: sort by id
		// use largest id that is not active as current
		// use largest id that is active as active

		//List<Pipeline> p = Arrays.asList(pipelines);
		//p.sort(Comparator.comparingLong(Pipeline::id));

		for (Pipeline pipeline : pipelines) {
			if (pipeline.hasActivity()) {
				if (active == null) {
					active = pipeline;
				}
				else {
					if (pipeline.getId() > active.getId()) {
						active = pipeline;
					}
				}
			}
			else {
				if (current == null) {
					current = pipeline;
				}
				else {
					if (pipeline.getId() > current.getId()) {
						current = pipeline;
					}
				}
			}
		}
		*/
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
