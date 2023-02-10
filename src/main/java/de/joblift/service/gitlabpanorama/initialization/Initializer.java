package de.joblift.service.gitlabpanorama.initialization;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.state.PipelinesState;
import de.joblift.service.gitlabpanorama.core.state.StateInitializedEvent;
import de.joblift.service.gitlabpanorama.filter.ResourceMatcher;
import de.joblift.service.gitlabpanorama.gitlab.GitlabFullstateCollector;
import de.joblift.service.gitlabpanorama.storage.local.LocalStorage;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;


/**
 * Initialization across several classes has to be performed in specific order. Therefore the PostConstruct is unified
 * in a single place.
 */
@Component
@AllArgsConstructor
public class Initializer {

	private InitializationConfiguration configuration;
	private EventBus eventbus;
	private GitlabFullstateCollector collector;
	private LocalStorage storage;
	private ResourceMatcher filter;
	private PipelinesState state;

	@PostConstruct
	public void init() {
		eventbus.register(state); // register this instance with the eventbus so it receives any events

		if (configuration.isLoadFromStorage()) {
			load().forEach(eventbus::post);
		}
		if (configuration.isCollectFromGitlab()) {
			new Thread(() -> {
				collector.refresh();
				eventbus.post(new StateInitializedEvent());
			}, "init collect").start();
		}
	}


	protected List<Pipeline> load() {
		return storage.load().stream()
			.filter(p -> !p.hasActivity())
			.filter(p -> filter.isProjectMatching(p.getProject().getPathNamespaced()))
			.filter(p -> filter.isRefMatching(p.getRef()))
			.collect(toList());
	}

}
