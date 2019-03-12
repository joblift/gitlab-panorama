package de.joblift.service.gitlabpanorama.core.aggregator;

import static de.galan.commons.time.Instants.*;
import static org.assertj.core.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import de.joblift.service.gitlabpanorama.core.models.Pipeline;
import de.joblift.service.gitlabpanorama.core.models.Project;
import de.joblift.service.gitlabpanorama.core.models.Status;


/**
 * CUT PipelinePair
 */
public class PipelinePairTest {

	private final static Instant T1 = instantUtc("2019-01-01T12:00:00Z");
	private final static Instant T2 = instantUtc("2019-02-02T12:00:00Z");
	private final static Instant T3 = instantUtc("2019-03-03T12:00:00Z");


	@Test
	public void singleCurrent() {
		Pipeline p = p(1L, Status.success);
		PipelinePair pp = new PipelinePair(p);
		assertThat(pp.getActive()).isNull();
		assertThat(pp.getCurrent()).isEqualTo(p);
	}


	@Test
	public void singleActive() {
		Pipeline p = p(1L, Status.running);
		PipelinePair pp = new PipelinePair(p);
		assertThat(pp.getActive()).isEqualTo(p);
		assertThat(pp.getCurrent()).isNull();
	}


	@Test
	public void singleActiveAndCurrent() {
		Pipeline pa = p(1L, Status.running);
		Pipeline pc = p(1L, Status.success);
		PipelinePair pp = new PipelinePair(pa, pc);
		assertThat(pp.getActive()).isEqualTo(pa);
		assertThat(pp.getCurrent()).isEqualTo(pc);
	}


	@Test
	public void currentFailedSucceeded() {
		Pipeline pa1 = p(1L, Status.running, T1);
		Pipeline pa2 = p(2L, Status.running, T2);
		Pipeline pc = p(1L, Status.success, T3);
		PipelinePair pp = new PipelinePair(pa1, pa2, pc);
		assertThat(pp.getActive()).isNull();
		assertThat(pp.getCurrent()).isEqualTo(pc);
	}


	@Test
	public void currentAfterRunning() {
		Pipeline pa1 = p(1L, Status.running, T1);
		Pipeline pa2 = p(2L, Status.running, T2);
		Pipeline pc = p(3L, Status.success, T3);
		PipelinePair pp = new PipelinePair(pa1, pa2, pc);
		assertThat(pp.getActive()).isNull();
		assertThat(pp.getCurrent()).isEqualTo(pc);
	}


	@Test
	public void currentSuccessSucceeded() {
		Pipeline pa1 = p(1L, Status.running, T1);
		Pipeline pa2 = p(2L, Status.running, T2);
		Pipeline pa3 = p(3L, Status.running, T2);
		Pipeline pc = p(3L, Status.success, T3);
		PipelinePair pp = new PipelinePair(pa1, pa2, pa3, pc);
		assertThat(pp.getActive()).isNull();
		assertThat(pp.getCurrent()).isEqualTo(pc);
	}


	@Test
	public void currentActiveFromRetry() {
		Pipeline pc = p(1L, Status.failed, T1);
		Pipeline pa = p(1L, Status.running, T2);
		PipelinePair pp = new PipelinePair(pa, pc);
		assertThat(pp.getActive()).isEqualTo(pa);
		assertThat(pp.getCurrent()).isEqualTo(pc);
	}


	@Test
	public void currentSuccessFromRetry() {
		Pipeline pc1 = p(1L, Status.failed, T1);
		Pipeline pa = p(1L, Status.running, T2);
		Pipeline pc2 = p(1L, Status.success, T3);
		PipelinePair pp = new PipelinePair(pc1, pa, pc2);
		assertThat(pp.getActive()).isNull();
		assertThat(pp.getCurrent()).isEqualTo(pc2);
	}


	@Test
	public void retrySuccess() {
		Pipeline pc1 = p(1L, Status.success, T1);
		Pipeline pc2 = p(1L, Status.success, T2);
		Pipeline pa = p(1L, Status.running, T3);
		PipelinePair pp = new PipelinePair(pc1, pc2, pa);
		assertThat(pp.getActive()).isEqualTo(pa);
		assertThat(pp.getCurrent()).isEqualTo(pc2);
	}


	private Pipeline p(Long id, Status status) {
		return p(id, status, T1);
	}


	private Pipeline p(Long id, Status status, Instant updated) {
		Project project = new Project();
		project.setName("dummy");

		Pipeline pipeline = new Pipeline();
		pipeline.setRef("master");
		pipeline.setProject(project);
		pipeline.setId(id);
		pipeline.setStatus(status);
		pipeline.setUpdated(updated);
		return pipeline;
	}

}
