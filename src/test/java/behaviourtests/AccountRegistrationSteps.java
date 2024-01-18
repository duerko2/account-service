package behaviourtests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import account.service.aggregate.Token;
import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.events.TokensAssigned;
import account.service.events.TokensRequested;
import account.service.repositories.AccountReadRepo;
import account.service.repositories.AccountRepo;
import account.service.service.AccountAlreadyExists;
import account.service.service.AccountService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Message;
import messaging.MessageQueue;

public class AccountRegistrationSteps {



	private AccountService service;
	private Account account;
	private AccountId accountId;
	private List<Message> publishedEvent = new ArrayList<>();


	private MessageQueue q = new MessageQueue() {

		@Override
		public void publish(Message event) {
			publishedEvent.add(event);
		}

		@Override
		public void addHandler(Class<? extends Message> eventType, Consumer<Message> handler) {
		}

	};
	public AccountRegistrationSteps() {
	}

	@Before
	public void setup(){
		AccountRepo accountRepo = new AccountRepo(q);
		AccountReadRepo accountReadRepo = new AccountReadRepo(q);
		service = new AccountService(q,accountRepo,accountReadRepo);

	}
//	@Given("there is an account with empty id")
//	public void thereIsAnAccountWithEmptyId() throws AccountAlreadyExists {
//		accountId = service.register("Test","testing",new AccountType("Customer"),"cpr123123");
//		account = service.getAccount(accountId);
//		assertNull(account.getAccountId());
//	}

	@When("the account is being registered")
	public void theAccountIsBeingRegistered() {
		// We have to run the registration in a thread, because
		// the register method will only finish after the next @When
		// step is executed.
			Account result;
			try {
				accountId = service.register("Test","testing",new AccountType("Customer"),"cpr123123","123123123123");
				account = service.getAccount(accountId);
			} catch (AccountAlreadyExists e) {
				throw new RuntimeException(e);
			}
	}

	@Then("the tokensRequestedEvent event is sent")
	public void theEventIsSent() {
		var tokensRequestedEvent = publishedEvent.stream().filter(e ->
			TokensRequested.class.isAssignableFrom(e.getClass()) && ((TokensRequested)e).getAccountId().equals(accountId)
		).collect(Collectors.toList());
		assertFalse(tokensRequestedEvent.isEmpty());

	}
	@When("the TokensAssigned event is sent with a list of {int} tokens")
	public void theEventIsSentWithNonEmptyId( Integer int1) {
		// This step simulate the event created by a downstream service.

		Set<Token> tokens = new HashSet<>();
		// adds 6 tokens to list
		for(int i = 0; i < int1; i++) {
			tokens.add(new Token(""));
		}
		TokensAssigned tokensAssignedEvent = new TokensAssigned(accountId,tokens);

		service.handleInitialTokensAssigned(tokensAssignedEvent);
	}

	@Then("the account is registered and his id is set and has {int} tokens")
	public void theAccountIsRegisteredAndHisIdIsSet(Integer int1) {
		assertNotNull(account.getAccountId());
		assertEquals(int1, service.getAccount(accountId).getTokens().size());
	}
	@After
	public void tearDown(){
		service.deleteAccount(account.getAccountId());
	}
}
