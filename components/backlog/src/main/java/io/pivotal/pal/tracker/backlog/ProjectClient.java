package io.pivotal.pal.tracker.backlog;

import org.springframework.web.client.RestOperations;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    public ProjectInfo getProject(long projectId) {
        return restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
    }
}
