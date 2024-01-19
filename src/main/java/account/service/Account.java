package account.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author: Rasmus Bo Thomsen
 * Mob programming, all members
 */
public class Account implements Serializable {

	private static final long serialVersionUID = 9023222981284806610L;
	private String name;
	private String lastname;
	private AccountType type;
	private String cpr;
	private String bankId;
	private String accountId;
	List<Token> tokens = new ArrayList<>();

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCpr() {
		return cpr;
	}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}


	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Account)) {
			return false;
		}
		var c = (Account) o;

		// check that account id is same
		return accountId != null && accountId.equals(c.getAccountId());
	}
}
