package de.joblift.service.gitlabpanorama.core.state;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.storage.local.LocalStorage;


/**
 * Listen to events, so the pipelines get stored for faster supply during bootstrap.
 */
@Component
public class StorageListener {

	private LocalStorage storage;
	private PipelinesState state;

	private boolean store = false;

	public StorageListener(LocalStorage storage, PipelinesState state, EventBus eventBus) {
		this.storage = storage;
		this.state = state;
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
