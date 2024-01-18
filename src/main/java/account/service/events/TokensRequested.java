package account.service.events;

import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokensRequested extends Event{
    private static final long serialVersionUID = 8022595332951627022L;
    private AccountId accountId;
    private int amount;
}
