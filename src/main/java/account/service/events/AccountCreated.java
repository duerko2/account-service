package account.service.events;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountCreated extends Event{
    private static final long serialVersionUID = -5924388505722740081L;
    private AccountId accountId;
    private String name;
    private String lastname;
    private AccountType type;
    private String cpr;
    private String bankId;

}
