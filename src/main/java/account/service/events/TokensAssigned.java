package account.service.events;

import account.service.aggregate.Token;
import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Set;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokensAssigned extends Event{
    private static final long serialVersionUID = -654639399553909420L;
    private AccountId accountId;
    private Set<Token> accountTokens;
}
