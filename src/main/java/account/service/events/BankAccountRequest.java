package account.service.events;

import account.service.aggregate.AccountId;
import account.service.aggregate.PaymentId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccountRequest extends Event{
    PaymentId paymentId;
    private AccountId customerId;
    private AccountId merchantId;
    private int amount;

    @Override
    public AccountId getAccountId() {
        return null;
    }
}