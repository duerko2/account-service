package account.service.events;

import account.service.aggregate.Token;
import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountTokenAdded extends Event {
    private static final long serialVersionUID = -890883092810023660L;
    private AccountId accountId;
    private Token token;

}
