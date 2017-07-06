package test.pivotal.pal.tracker.accounts;

import io.pivotal.pal.tracker.accounts.RegistrationController;
import io.pivotal.pal.tracker.accounts.RegistrationForm;
import io.pivotal.pal.tracker.accounts.RegistrationService;
import io.pivotal.pal.tracker.users.UserInfo;
import io.pivotal.pal.tracker.users.data.UserRecord;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

    private RegistrationService registrationService = mock(RegistrationService.class);
    private RegistrationController registrationController = new RegistrationController(registrationService);

    @Test
    public void create() {
        UserRecord userRecord = new UserRecord(24L, "Billy");
        doReturn(userRecord).when(registrationService).createUserWithAccount(any());


        UserInfo result = registrationController.create(new RegistrationForm("Billy"));


        verify(registrationService).createUserWithAccount("Billy");
        assertThat(result).isEqualTo(new UserInfo(24L, "Billy", "registration info"));
    }
}
