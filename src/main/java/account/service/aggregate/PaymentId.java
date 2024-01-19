package account.service.aggregate;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;
import java.util.UUID;

@ValueObject
@Value
public class PaymentId implements Serializable {
    private static final long serialVersionUID=-1258555970416660835L;

    private UUID uuid;

}