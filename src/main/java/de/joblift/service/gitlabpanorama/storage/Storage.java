package de.joblift.service.gitlabpanorama.storage;

import java.util.Collection;
import java.util.List;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;


/**
 * Contract for a storage implementation
 */
public interface Storage {

	List<Pipeline> load();


	void store(Collection<Pipeline> pipelines);

}
