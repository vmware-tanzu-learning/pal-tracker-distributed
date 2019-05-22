package test.pivotal.pal.tracker.registration;

import io.pivotal.pal.tracker.registration.App;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationAppTest {

    @Test
    public void embedded() {
        App.main(new String[]{});

        RestTemplate restTemplate = new RestTemplate();

        assertThat(restTemplate.getForObject("http://localhost:8181/accounts?ownerId=0", String.class)).isEqualTo("[]");
        assertThat(restTemplate.getForObject("http://localhost:8181/projects?accountId=0", String.class)).isEqualTo("[]");
        assertThat(restTemplate.getForObject("http://localhost:8181/projects/0", String.class)).isEqualTo(null);
        assertThat(restTemplate.getForObject("http://localhost:8181/users/0", String.class)).isEqualTo(null);
    }
}
