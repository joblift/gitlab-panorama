package de.joblift.service.gitlabpanorama.storage.local;

import static java.nio.charset.StandardCharsets.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.storage.Storage;
import de.joblift.service.gitlabpanorama.util.Mapper;


/**
 * Storage on a local path
 */
@Component
public class LocalStorage implements Storage {

	@Autowired
	LocalStorageConfiguration configuration;

	private final static String STATE_FILENAME = "pipeline-states.json";


	@Override
	public List<Pipeline> load() {
		List<Pipeline> result = new ArrayList<>();
		if (configuration.isEnabled()) {
			try {
				File file = new File(getPath(), STATE_FILENAME);
				if (file.exists()) {
					Say.info("Loading locally stored files from {}", file.getAbsolutePath());
					String content = FileUtils.readFileToString(file, UTF_8);
					Pipeline[] pipelines = Mapper.get().readValue(content, Pipeline[].class);
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
				FileUtils.write(file, Mapper.get().writeValueAsString(pipelines), UTF_8);
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
