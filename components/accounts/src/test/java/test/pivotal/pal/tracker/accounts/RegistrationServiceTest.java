package test.pivotal.pal.tracker.accounts;

import io.pivotal.pal.tracker.accounts.RegistrationService;
import io.pivotal.pal.tracker.accounts.data.AccountDataGateway;
import io.pivotal.pal.tracker.users.data.UserDataGateway;
import io.pivotal.pal.tracker.users.data.UserRecord;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    private UserDataGateway userDataGateway = mock(UserDataGateway.class);
    private AccountDataGateway accountDataGateway = mock(AccountDataGateway.class);
    private RegistrationService service = new RegistrationService(userDataGateway, accountDataGateway);

    @Test
    public void testCreateUserWithAccount() {
        UserRecord createdUser = new UserRecord(22L, "Some User");
        doReturn(createdUser).when(userDataGateway).create("Some User");


        UserRecord result = service.createUserWithAccount("Some User");


        verify(userDataGateway).create("Some User");
        verify(accountDataGateway).create(22L, "Some User's account");

        UserRecord expectedResult = new UserRecord(22L, "Some User");
        assertThat(result).isEqualTo(expectedResult);
    }
}
