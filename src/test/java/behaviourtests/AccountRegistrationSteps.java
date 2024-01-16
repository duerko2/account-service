package behaviourtests;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import account.service.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;

public class AccountRegistrationSteps {

	private CompletableFuture<Event> publishedEvent = new CompletableFuture<>();

	private MessageQueue q = new MessageQueue() {

		@Override
		public void publish(Event event) {
			publishedEvent.complete(event);
		}

		@Override
		public void addHandler(String eventType, Consumer<Event> handler) {
		}
		
	};
	private AccountService service = new AccountService(q);
	private CompletableFuture<Account> registeredAccount = new CompletableFuture<>();
	private Account account;

	public AccountRegistrationSteps() {
	}

	@Given("there is an account with empty id")
	public void thereIsAnAccountWithEmptyId() {
		account = new Account();
		account.setLastname("Bond");
		//...
		account.setName("James");// Bond
		account.setCpr("007");
		account.setType(new AccountType("customer"));
		assertNull(account.getAccountId());
	}

	@When("the account is being registered")
	public void theAccountIsBeingRegistered() {
		// We have to run the registration in a thread, because
		// the register method will only finish after the next @When
		// step is executed.
		new Thread(() -> {
			Account result;
			try {
				registeredAccount = service.register(account);
			} catch (AccountAlreadyExists e) {
				throw new RuntimeException(e);
			}
		}).start();
	}

	@Then("the {string} event is sent")
	public void theEventIsSent(String string) {
		Event event = new Event(string, new Object[] { account });
		assertEquals(event,publishedEvent.join());
	}
	@When("the {string} event is sent with a list of {int} tokens")
	public void theEventIsSentWithNonEmptyId(String string, Integer int1) {
		// This step simulate the event created by a downstream service.
		var c = new Account();
		c.setName(account.getName());
		c.setLastname(account.getLastname());
		c.setCpr(account.getCpr());
		c.setAccountId(account.getAccountId());
		c.setType(new AccountType("customer"));
		List<Token> tokens = new ArrayList<>();
		// adds 6 tokens to list
		for(int i = 0; i < int1; i++) {
			tokens.add(new Token(""));
		}
		c.setTokens(tokens);

		service.handleInitialTokensAssigned(new Event(string,new Object[] {c}));
	}

	@Then("the account is registered and his id is set and has {int} tokens")
	public void theAccountIsRegisteredAndHisIdIsSet(Integer int1) {
		assertNotNull(registeredAccount.join().getAccountId());
		assertEquals(int1, registeredAccount.join().getTokens().size());
	}
	@After
	public void tearDown(){
		service.deleteAccount(account.getAccountId());
	}
}
