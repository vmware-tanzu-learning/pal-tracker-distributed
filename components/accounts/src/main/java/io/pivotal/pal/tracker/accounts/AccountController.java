package io.pivotal.pal.tracker.accounts;

import io.pivotal.pal.tracker.accounts.data.AccountDataGateway;
import io.pivotal.pal.tracker.accounts.data.AccountRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.pivotal.pal.tracker.accounts.AccountInfo.accountInfoBuilder;
import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    private final AccountDataGateway gateway;

    public AccountController(AccountDataGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping("/accounts")
    public List<AccountInfo> list(@RequestParam long ownerId) {
        return gateway.findAllByOwnerId(ownerId)
            .stream()
            .map(this::present)
            .collect(toList());
    }

    private AccountInfo present(AccountRecord record) {
        return accountInfoBuilder()
            .id(record.id)
            .ownerId(record.ownerId)
            .name(record.name)
            .info("account info")
            .build();
    }
}
