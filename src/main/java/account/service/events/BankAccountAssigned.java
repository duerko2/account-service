package account.service.events;

import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serial;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccountAssigned extends Event{
    private static final long serialVersionUID = -3281411080003071594L;
    private AccountId accountId;
    private String bankId;
}
