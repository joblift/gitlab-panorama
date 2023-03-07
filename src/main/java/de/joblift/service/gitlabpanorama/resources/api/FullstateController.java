package de.joblift.service.gitlabpanorama.resources.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.eventbus.EventBus;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.models.CleanStateEvent;
import de.joblift.service.gitlabpanorama.gitlab.GitlabFullstateCollector;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/state/refresh")
public class FullstateController {

	private GitlabFullstateCollector collector;
	private EventBus eventbus;

	@RequestMapping
	public String refresh() {
		Say.info("Triggered refresh via rest");
		new Thread(() -> {
			eventbus.post(new CleanStateEvent());
			collector.refresh();
		}, "refresh collect").start();
		return "done";
	}

}
