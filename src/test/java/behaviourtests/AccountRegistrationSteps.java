package behaviourtests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.*;
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
import account.service.repositories.QueueTranslator;
import account.service.service.AccountAlreadyExists;
import account.service.service.AccountService;
import com.google.gson.internal.LinkedTreeMap;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import message.Message;
import message.MessageQueue;
import message.implementations.MessageQueueSync;
import messaging.Event;
import org.junit.jupiter.api.Assertions;

/**
 * @Author: Marcus Jacobsen
 * Pair programming with Alexander Elsing
 * Based on code from Hubert Baumeister
 */
public class AccountRegistrationSteps {



	private AccountService service;
	private Account account;
	private AccountId accountId;
	private List<Event> publishedEvent = new ArrayList<>();
	private MessageQueue queue = new MessageQueueSync();

	private messaging.MessageQueue q = new messaging.MessageQueue(){

		@Override
		public void publish(Event event) {
			publishedEvent.add(event);
		}

		@Override
		public void addHandler(String eventType, Consumer<Event> handler) {
		}

	};
	public AccountRegistrationSteps() {
	}

	@Before
	public void setup(){
		AccountRepo accountRepo = new AccountRepo(queue);
		AccountReadRepo accountReadRepo = new AccountReadRepo(queue);
		service = new AccountService(queue,q,accountRepo,accountReadRepo);

	}


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
		var events = publishedEvent.stream().filter(e-> e.getType().equals("InitialTokensRequested")).collect(Collectors.toList());
		Assertions.assertFalse(events.isEmpty());

	}
	@When("the TokensAssigned event is sent with a list of {int} tokens")
	public void theEventIsSentWithNonEmptyId( Integer int1) {
		// This step simulate the event created by a downstream service.

		List<Token> tokens = new ArrayList<>();
		// adds 6 tokens to list
		for(int i = 0; i < int1; i++) {
			tokens.add(new Token(UUID.randomUUID().toString()));
		}
		Event event = new Event("InitialTokensAssigned",new Object[]{accountId.toString(),tokens});
		service.handleTokensAssigned(event);
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
