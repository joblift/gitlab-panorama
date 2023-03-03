package de.joblift.service.gitlabpanorama.storage.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.storage.Storage;
import de.joblift.service.gitlabpanorama.util.Mapper;

import lombok.AllArgsConstructor;


/**
 * Storage on a local path
 */
@Component
@AllArgsConstructor
public class LocalStorage implements Storage {

	private final static String STATE_FILENAME = "pipeline-states.json";

	private LocalStorageConfiguration configuration;

	@Override
	public List<Pipeline> load() {
		List<Pipeline> result = new ArrayList<>();
		if (configuration.isEnabled()) {
			try {
				File file = new File(getPath(), STATE_FILENAME);
				if (file.exists()) {
					Say.info("Loading locally stored files from {}", file.getAbsolutePath());
					Pipeline[] pipelines = Mapper.get().readValue(file, Pipeline[].class);
					result = Arrays.asList(pipelines);
					Say.info("Found {} locally stored pipelines", result.size());
				}
				else {
					Say.info("No state-file available in {}, skipping", file.getAbsolutePath());
				}
			}
			catch (Exception ex) {
				Say.error("Failed loading pipelines from local storage '" + configuration.getPath() + "'", ex);
			}
		}
		return result;
	}


	@Override
	public synchronized void store(Collection<Pipeline> pipelines) {
		if (configuration.isEnabled()) {
			try {
				File file = new File(getPath(), STATE_FILENAME);
				Say.info("Storing state to file {}", file.getAbsolutePath());
				Mapper.get().writeValue(file, pipelines);
			}
			catch (IOException ex) {
				Say.warn("Failed storing state file in '" + configuration.getPath() + "'", ex);
			}
		}
	}


	private File getPath() {
		File path = new File(configuration.getPath());
		if (!path.exists()) {
			path.mkdirs();
		}
		return path;
	}

}
