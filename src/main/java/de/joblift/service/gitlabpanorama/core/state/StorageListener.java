package de.joblift.service.gitlabpanorama.core.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.storage.local.LocalStorage;


/**
 * Listenes to events, so the pipelines get stored for faster supply during bootstrap.
 */
@Component
public class StorageListener {

	@Autowired
	LocalStorage storage;

	@Autowired
	PipelinesState state;

	private boolean store = false;


	@Autowired
	public StorageListener(EventBus eventBus) {
		eventBus.register(this); // register this instance with the eventbus so it receives any events
	}


	@Subscribe
	public void onStateInitialized(StateInitializedEvent event) {
		Say.info("Received StateInitializedEvent");
		store = true;
		store();
	}


	@Subscribe
	public void onPipeline(Pipeline pipeline) {
		if (store) {
			store();
		}
	}


	private void store() {
		storage.store(state.getPipelines());
	}

}
