package de.joblift.service.gitlabpanorama.gitlab;

import static java.nio.charset.StandardCharsets.*;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import de.galan.commons.logging.Say;
import de.galan.commons.net.flux.FluentHttpClient.HttpBuilder;
import de.galan.commons.net.flux.Flux;
import de.galan.commons.net.flux.Response;
import de.joblift.service.gitlabpanorama.filter.ResourceMatcher;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabBranch;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineComplete;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabPipelineMinimal;
import de.joblift.service.gitlabpanorama.gitlab.model.GitlabProject;
import de.joblift.service.gitlabpanorama.util.Mapper;


@Component
public class GitlabClient {

	long requestCounter = 0;

	@Autowired
	GitlabConfiguration configuration = new GitlabConfiguration();

	Boolean filterTags = true;

	@Autowired
	ResourceMatcher filter;


	public GitlabClient() {
		Flux.setDefaultTimeoutRead(configuration.getTimeout());
	}


	public long getRequestCount() {
		return requestCounter;
	}


	private List<GitlabProject> retrieveProjects(int page) {
		HttpBuilder http = Flux.request(configuration.getEndpoint() + "/projects")
			.param("private_token", configuration.getToken())
			.param("membership", "true")
			.param("per_page", "100")
			.param("page", "" + page)
			.param("archived", "false");
		Say.info("Querying projects, page {}", page);
		try (Response response = http.get()) {
			requestCounter++;
			GitlabProject[] projects = Mapper.get().readValue(response.getStreamAsString(), GitlabProject[].class);
			List<GitlabProject> collect = Arrays.asList(projects).stream()
				.filter(p -> filter.isProjectMatching(p.getPathNamespaced()))
				.filter(GitlabProject::isJobsEnabled)
				.collect(toList());
			if (projects.length > 0) {
				collect.addAll(retrieveProjects(page + 1));
			}
			return collect;
		}
		catch (Exception ex) {
			Say.error("Failed retrieving projects from gitlab", ex);
		}
		return null;
	}


	public List<GitlabProject> retrieveProjects() {
		return retrieveProjects(1);
	}


	public List<GitlabPipelineMinimal> retrieveProjectPipelineMinimals(GitlabProject project) {
		List<GitlabPipelineMinimal> result = new ArrayList<>();
		HttpBuilder http = Flux.request(configuration.getEndpoint() + "/projects/" + project.getId() + "/pipelines")
			.param("private_token", configuration.getToken())
			.param("yaml_errors", "false");
		Say.info("Querying pipelines for project {}", project.getPathNamespaced());
		try (Response response = http.get()) {
			requestCounter++;
			GitlabPipelineMinimal[] pipelines = Mapper.get().readValue(response.getStreamAsString(), GitlabPipelineMinimal[].class);
			List<GitlabPipelineMinimal> collect = Arrays.asList(pipelines).stream().peek(pipeline -> pipeline.setProject(project)).collect(toList());
			// identify the last states per ref, and add them to the result
			ListMultimap<String, GitlabPipelineMinimal> index = Multimaps.index(collect, p -> p.getRef() + "/" + p.getStatus());
			for (String key : index.keySet()) {
				List<GitlabPipelineMinimal> sorting = new ArrayList<>(index.get(key));
				sorting.sort(comparing(GitlabPipelineMinimal::getId).reversed());
				result.add(sorting.get(0));
			}
		}
		catch (Exception ex) {
			Say.error("Failed retrieving pipline overview from gitlab", ex);
		}
		return result;
	}


	public List<GitlabPipelineComplete> retrievePipelineDetails(List<GitlabPipelineMinimal> minimals) {
		List<GitlabPipelineComplete> result = new ArrayList<>();
		for (GitlabPipelineMinimal minimal : minimals) {
			GitlabPipelineComplete pipeline = retrievePipelineComplete(minimal.getProject(), minimal.getRef(), minimal.getId());
			if (pipeline != null) {
				result.add(pipeline);
			}
		}
		return result;
	}


	public GitlabPipelineComplete retrievePipelineComplete(GitlabProject project, String ref, Long pipelineId) {
		GitlabPipelineComplete result = null;
		HttpBuilder http = Flux.request(configuration.getEndpoint() + "/projects/" + project.getId() + "/pipelines/" + pipelineId)
			.param("private_token", configuration.getToken())
			.param("yaml_errors", "false");
		Say.info("Querying pipeline details for pipeline {}, {}, {}", project.getPathNamespaced(), ref, pipelineId);
		try (Response response = http.get()) {
			requestCounter++;
			GitlabPipelineComplete complete = Mapper.get().readValue(response.getStreamAsString(), GitlabPipelineComplete.class);
			complete.setProject(project);
			if (!(complete.isTag() && filterTags)) {
				result = complete;
			}
		}
		catch (Exception ex) {
			Say.error("Failed retrieving pipline details from gitlab", ex);
		}
		return result;
	}


	public GitlabBranch retrieveBranch(GitlabProject project, String ref) {
		GitlabBranch result = null;

		HttpBuilder http = Flux.request(configuration.getEndpoint() + "/projects/" + project.getId() + "/repository/branches/" + encUrl(ref))
			.param("private_token", configuration.getToken());
		Say.info("Querying branch details for ref {}, {}", project.getPathNamespaced(), ref);
		try (Response response = http.get()) {
			requestCounter++;
			if (response.isSucceded()) {
				result = Mapper.get().readValue(response.getStreamAsString(), GitlabBranch.class);
			}
		}
		catch (Exception ex) {
			Say.error("Failed retrieving pipline details from gitlab", ex);
		}
		return result;
	}


	private String encUrl(String input) {
		try {
			return URLEncoder.encode(input, UTF_8.name());
		}
		catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

}
