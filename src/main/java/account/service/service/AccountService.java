package account.service.service;

import account.service.aggregate.*;
import account.service.events.*;
import account.service.repositories.AccountReadRepo;
import account.service.repositories.AccountRepo;
import account.service.repositories.QueueTranslator;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import message.MessageQueue;
import messaging.Event;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class AccountService {

	private MessageQueue queue;
	private messaging.MessageQueue rabbitQueue;

	AccountRepo accountRepo;
	AccountReadRepo accountReadRepo;
	
	public AccountService(MessageQueue q, messaging.MessageQueue rabbitQueue, AccountRepo accountRepo, AccountReadRepo accountReadRepo) {
		queue = q;
		this.rabbitQueue = rabbitQueue;
		this.accountRepo = accountRepo;
		this.accountReadRepo = accountReadRepo;

		rabbitQueue.addHandler("InitialTokensAssigned" ,this::handleTokensAssigned);
		rabbitQueue.addHandler("NewTokenRequestedAssigned" ,this::handleTokensAssigned);
		rabbitQueue.addHandler("PaymentRequestValidated" ,this::handleBankAccountsAssignmentRequest);

	}

	public AccountId register(String firstName, String lastName, AccountType type, String cpr, String bankId) throws AccountAlreadyExists {


		// check if account exists in repo
		if(accountReadRepo.accountExists(cpr)){
			throw new AccountAlreadyExists("Account already exists");
		}

		Account account = Account.createAccount(firstName,lastName,type,cpr,bankId);

		accountRepo.save(account);

		Event tokensRequestedEvent = new Event("InitialTokensRequested",new Object[]{account.getAccountId().toString(),6});
		rabbitQueue.publish(tokensRequestedEvent);

		System.out.println("Account created with" + account.getAccountId());
		return account.getAccountId();
	}



	public void handleTokensAssigned(Event e) {
		String accountId = e.getArgument(0,String.class);
		var jsonTokens = e.getArgument(1, List.class);
		Set<Token> tokens = new HashSet<>();
		Gson gson = new Gson();
		for (var jToken: jsonTokens){
			tokens.add(gson.fromJson(jToken.toString(),Token.class));
		}

		System.out.println("TokensAssigned event received");
		Account account = accountRepo.getAccount( new AccountId(UUID.fromString(accountId)));

		account.UpdateAccountTokens(tokens.stream().collect(Collectors.toSet()));
		accountRepo.save(account);
	}

	public void handleBankAccountsAssignmentRequest(Event event) {
		Payment payment = event.getArgument(0,Payment.class);
		var customer = accountRepo.getAccount(new AccountId(UUID.fromString(payment.getCustomerId())));
		var merchant = accountRepo.getAccount(new AccountId(UUID.fromString(payment.getMerchantId())));
		payment.setCustomerBankId(customer.getBankId());
		payment.setMerchantBankId(merchant.getBankId());
		Event banksAssignedEvent = new Event("BankAccountsAssigned", new Object[] { payment });
		rabbitQueue.publish(banksAssignedEvent);
	}

	public void deleteAccount(AccountId accountId) {
		accountRepo.deleteAccount(accountId);
	}

	public Account getAccount(AccountId accountId) {
		return accountRepo.getAccount(accountId);
	}

	public void generateTokens(String accountId, int amount){
		Event event = new Event("NewTokenRequestRequested",new Object[]{accountId,amount});
		rabbitQueue.publish(event);

	}

	public List<Token> getTokens(AccountId accountId) {
		System.out.println("accountId = " + accountId);
		Account account = accountRepo.getAccount(accountId);
		System.out.printf("Account found with name" + account.getName());
		List <Token> t = accountRepo.getAccount(accountId).getTokens().stream().collect(Collectors.toList());
		System.out.println("t = " + t);
		System.out.println("t.size() = " + t.size());
		return t;
	}
}
