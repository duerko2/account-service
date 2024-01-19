package account.service;

import java.util.concurrent.CompletableFuture;

import messaging.Event;
import messaging.MessageQueue;
import java.util.UUID;
/**
 * @Author: Marcus Jacobsen
 * Mob programming, all members
 */
public class AccountService {

	private MessageQueue queue;

	AccountRepo accountRepo = new AccountRepo();
	
	public AccountService(MessageQueue q) {
		queue = q;
		queue.addHandler("InitialTokensAssigned", this::handleInitialTokensAssigned);
		queue.addHandler("PaymentRequestValidated", this::handleBankAccountsAssignmentRequest);
	}

	public CompletableFuture<Account> register(Account requestedAccount) throws AccountAlreadyExists {
		CompletableFuture<Account> registeredAccount = new CompletableFuture<>();

		// check if account exists in repo
		if(accountRepo.accountExists(requestedAccount.getCpr())){
			throw new AccountAlreadyExists("Account already exists");
		}


		String newAccountNumber = UUID.randomUUID().toString();
		requestedAccount.setAccountId(newAccountNumber);


		if(requestedAccount.getType().getType().equals("customer")){
			// Customer account gets stored as account request
			accountRepo.storeAccountRequest(newAccountNumber,registeredAccount);
			registerCustomerAccount(requestedAccount);
		} else {
			// Merchant account gets stored right away in repo
			registeredAccount.complete(registerMerchantAccount(requestedAccount));
			accountRepo.storeAccount(requestedAccount);
		}

		// Return the completable future to the resource (merchant is already completed)
		return registeredAccount.orTimeout(5, java.util.concurrent.TimeUnit.SECONDS);
	}

	private Account registerMerchantAccount(Account s) {
		return s;
	}

	private void registerCustomerAccount(Account s) {
		Event event = new Event("InitialTokensRequested", new Object[] { s,6 });
		queue.publish(event);
	}

	public void handleInitialTokensAssigned(Event e) {
		var s = e.getArgument(0, Account.class);
		accountRepo.getAccountRequest(s.getAccountId()).complete(s);
		accountRepo.storeAccount(s);
	}

	public void handleBankAccountsAssignmentRequest(Event e) {
		var p = e.getArgument(0, Payment.class);
		var customerBankId = accountRepo.getAccount(p.getCustomerId()).getBankId();
		var merchantBankId = accountRepo.getAccount(p.getMerchantId()).getBankId();

		p.setCustomerBankId(customerBankId);
		p.setMerchantBankId(merchantBankId);

		Event event = new Event("BankAccountsAssigned", new Object[] { p });
		queue.publish(event);
	}

	public void deleteAccount(String accountId) {
		accountRepo.deleteAccount(accountId);
	}

	public Account getAccount(String accountId) {
		return accountRepo.getAccount(accountId);
	}
}
