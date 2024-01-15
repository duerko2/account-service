package account.service;

public class AccountType {
    public String type;
    public AccountType() {
    }
    public AccountType(String string) {
        type = string;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
