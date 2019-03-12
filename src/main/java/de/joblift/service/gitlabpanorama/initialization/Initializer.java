package de.joblift.service.gitlabpanorama.initialization;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.state.PipelinesState;
import de.joblift.service.gitlabpanorama.core.state.StateInitializedEvent;
import de.joblift.service.gitlabpanorama.filter.ResourceMatcher;
import de.joblift.service.gitlabpanorama.gitlab.GitlabFullstateCollector;
import de.joblift.service.gitlabpanorama.storage.local.LocalStorage;


/**
 * Initialization accros several classes has to be performed in specific order. Therefore the PostConstruct is unified
 * in a single place.
 */
@Component
public class Initializer {

	@Autowired
	InitializationConfiguration configuration;

	@Autowired
	EventBus eventbus;

	@Autowired
	GitlabFullstateCollector collector;

	@Autowired
	LocalStorage storage;

	@Autowired
	ResourceMatcher filter;

	@Autowired
	PipelinesState state;


	@PostConstruct
	public void init() {
		eventbus.register(state); // register this instance with the eventbus so it receives any events

		if (configuration.isLoadFromStorage()) {
			load().stream().forEach(eventbus::post);
		}
		if (configuration.isCollectFromGitlab()) {
			new Thread(() -> {
				collector.refresh();
				eventbus.post(new StateInitializedEvent());
			}, "init collect").start();
		}
	}


	protected List<Pipeline> load() {
		List<Pipeline> result = storage.load().stream()
			.filter(p -> !p.hasActivity())
			.filter(p -> filter.isProjectMatching(p.getProject().getPathNamespaced()))
			.filter(p -> filter.isRefMatching(p.getRef()))
			.collect(toList());
		return result;
	}

}
