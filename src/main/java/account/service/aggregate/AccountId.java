package account.service.aggregate;

import java.io.Serializable;
import java.util.UUID;

import org.jmolecules.ddd.annotation.ValueObject;

import lombok.Value;

@ValueObject
@Value
public class AccountId implements Serializable{
	private static final long serialVersionUID = -1455308747700082116L;
	private UUID uuid;


}
