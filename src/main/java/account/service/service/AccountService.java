package account.service.service;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.aggregate.Token;
import account.service.events.*;
import account.service.repositories.AccountReadRepo;
import account.service.repositories.AccountRepo;
import io.cucumber.messages.internal.com.google.common.base.Joiner;
import messaging.MessageQueue;

import java.util.List;

public class AccountService {

	private MessageQueue queue;

	AccountRepo accountRepo;
	AccountReadRepo accountReadRepo;
	
	public AccountService(MessageQueue q,AccountRepo accountRepo,AccountReadRepo accountReadRepo) {
		queue = q;
		this.accountRepo = accountRepo;
		this.accountReadRepo = accountReadRepo;
		queue.addHandler(TokensAssigned.class, e -> handleTokensAssigned((TokensAssigned) e));
		queue.addHandler(BankAccountRequest.class, e -> handleBankAccountsAssignmentRequest((BankAccountRequest) e));
	}

	public AccountId register(String firstName, String lastName, AccountType type, String cpr, String bankId) throws AccountAlreadyExists {


		// check if account exists in repo
		if(accountReadRepo.accountExists(cpr)){
			throw new AccountAlreadyExists("Account already exists");
		}

		Account account = Account.createAccount(firstName,lastName,type,cpr,bankId);

		accountRepo.save(account);

		TokensRequested tokensRequestedEvent = new TokensRequested(account.getAccountId(),6);
		queue.publish(tokensRequestedEvent);

		return account.getAccountId();
	}



	public void handleTokensAssigned(TokensAssigned e) {
		System.out.println("TokensAssigned event received");
		Account account = accountRepo.getAccount(e.getAccountId());
		account.UpdateAccountTokens(e.getAccountTokens());
		accountRepo.save(account);
	}

	public void handleBankAccountsAssignmentRequest(BankAccountRequest event) {
		var customer = accountRepo.getAccount(event.getAccountId());
		var merchant = accountRepo.getAccount(event.getMerchantId());

		var banksAssignedEvent = new BankAccountAssigned(event.getPaymentId(),customer.getBankId(), merchant.getBankId(),event.getAmount());
		queue.publish(banksAssignedEvent);
	}

	public void deleteAccount(AccountId accountId) {
		accountRepo.deleteAccount(accountId);
	}

	public Account getAccount(AccountId accountId) {
		return accountRepo.getAccount(accountId);
	}

	public List<Token> getTokens(AccountId accountId) {
		System.out.println("accountId = " + accountId);
		List <Token> t = accountRepo.getTokens(accountId);
		System.out.println("t = " + t);
		System.out.println("t.size() = " + t.size());
		return t;
	}
}
