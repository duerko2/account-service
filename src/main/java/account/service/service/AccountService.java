package account.service.service;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.events.*;
import account.service.repositories.AccountReadRepo;
import account.service.repositories.AccountRepo;
import messaging.MessageQueue;

public class AccountService {

	private MessageQueue queue;

	AccountRepo accountRepo;
	AccountReadRepo accountReadRepo;
	
	public AccountService(MessageQueue q,AccountRepo accountRepo,AccountReadRepo accountReadRepo) {
		queue = q;
		this.accountRepo = accountRepo;
		this.accountReadRepo = accountReadRepo;
		queue.addHandler(TokensAssigned.class, e -> handleInitialTokensAssigned((TokensAssigned) e));
		queue.addHandler(BankAccountRequest.class, e -> handleBankAccountsAssignmentRequest((BankAccountRequest) e));
	}

	public AccountId register(String firstName, String lastName, AccountType type, String cpr, String bankId) throws AccountAlreadyExists {

		Account account = Account.createAccount(firstName,lastName,type,cpr,bankId);

		// check if account exists in repo
		if(accountReadRepo.accountExists(account.getCpr())){
			throw new AccountAlreadyExists("Account already exists");
		}
		accountRepo.save(account);

		TokensRequested tokensRequestedEvent = new TokensRequested(account.getAccountId(),6);
		queue.publish(tokensRequestedEvent);

		return account.getAccountId();
	}



	public void handleInitialTokensAssigned(TokensAssigned e) {
		Account account = accountRepo.getAccount(e.getAccountId());
		account.UpdateAccountTokens(e.getAccountTokens());
		accountRepo.save(account);
	}

	public void handleBankAccountsAssignmentRequest(BankAccountRequest event) {
		var customer = accountRepo.getAccount(event.getAccountId());
		var merchant = accountRepo.getAccount(event.getMerchantId());

		var customerEvent = new BankAccountAssigned(customer.getAccountId(),customer.getBankId());
		var merchantEvent = new BankAccountAssigned(merchant.getAccountId(), merchant.getBankId());
		queue.publish(customerEvent);
		queue.publish(merchantEvent);

	}

	public void deleteAccount(AccountId accountId) {
		accountRepo.deleteAccount(accountId);

	}

	public Account getAccount(AccountId accountId) {
		return accountRepo.getAccount(accountId);
	}
}
