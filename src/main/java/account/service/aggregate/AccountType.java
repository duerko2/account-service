package account.service.aggregate;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;

@ValueObject
@Value
public class AccountType implements Serializable {
    public String type;

}
