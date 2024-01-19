package account.service.rest.adapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO{
    private String accountId;
    private String name;
    private String lastName;
    private String type;
    private String cpr;
    private String bankId;
    public AccountDTO() {
    }
    public AccountDTO(String accountId, String name, String lastName, String type, String cpr, String bankId) {
        this.accountId = accountId;
        this.name = name;
        this.lastName = lastName;
        this.type = type;
        this.cpr = cpr;
        this.bankId = bankId;
    }

}
