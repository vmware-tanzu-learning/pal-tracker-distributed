package io.pivotal.pal.tracker.timesheets;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private Map<Long,ProjectInfo> cache;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.cache = new ConcurrentHashMap<>();
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }
    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo info =  restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        this.cache.put(projectId,info);
        return info;
    }

    public ProjectInfo getProjectFromCache(long projectId){
        return this.cache.get(projectId);
    }
}
