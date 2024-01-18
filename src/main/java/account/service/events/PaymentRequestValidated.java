package account.service.events;

import account.service.aggregate.AccountId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serial;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaymentRequestValidated extends Event{
    private static final long serialVersionUID = -2643561098073700754L;
    private AccountId accountId;
}
