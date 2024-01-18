package account.service.events;

import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccountRequest extends Event{
    private AccountId accountId;
    private AccountId merchantId;

}
