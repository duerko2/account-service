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
//		queue.addHandler("PaymentRequestValidated", this::handleBankAccountsAssignmentRequest);
	}

	public AccountId register(String firstName, String lastName, AccountType type, String cpr) throws AccountAlreadyExists {

		Account account = Account.createAccount(firstName,lastName,type,cpr);

		// check if account exists in repo
		if(accountReadRepo.accountExists(account.getCpr())){
			throw new AccountAlreadyExists("Account already exists");
		}
		accountRepo.save(account);

		TokensRequested tokensRequestedEvent = new TokensRequested(account.getAccountId(),6);
		queue.publish(tokensRequestedEvent);

		return account.getAccountId();
	}

	private Account registerMerchantAccount(Account s) {
		return s;
	}

//	private void registerCustomerAccount(Account s) {
//		Event event = new Event("InitialTokensRequested", new Object[] { s });
//		//queue.publish(event);
//	}

	public void handleInitialTokensAssigned(TokensAssigned e) {
		Account account = accountRepo.getAccount(e.getAccountId());
		account.UpdateAccountTokens(e.getAccountTokens());
		accountRepo.save(account);
	}

//	public void handleBankAccountsAssignmentRequest(Event e) {
//		var p = e.getArgument(0, Payment.class);
//		var customerBankId = accountRepo.getAccount(p.getCustomerId()).getBankId();
//		var merchantBankId = accountRepo.getAccount(p.getMerchantId()).getBankId();
//
//		p.setCustomerBankId(customerBankId);
//		p.setMerchantBankId(merchantBankId);
//
//		Event event = new Event("BankAccountsAssigned", new Object[] { p });
//		queue.publish(event);
//	}

	public void deleteAccount(AccountId accountId) {
		accountRepo.deleteAccount(accountId);
	}

	public Account getAccount(AccountId accountId) {
		return accountRepo.getAccount(accountId);
	}
}
