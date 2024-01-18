package account.service.events;

import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountDeleted extends Event{
    private static final long serialVersionUID = 731315567449074317L;
    private AccountId accountId;
}
