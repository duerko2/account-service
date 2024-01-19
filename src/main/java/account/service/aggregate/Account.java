package account.service.aggregate;

import account.service.events.AccountCreated;
import account.service.events.AccountDeleted;
import account.service.events.AccountTokenAdded;
import account.service.events.Event;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import lombok.AccessLevel;
import lombok.Getter;

import lombok.Setter;
import message.Message;
import org.jboss.resteasy.annotations.providers.jaxb.IgnoreMediaTypes;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;

import javax.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@AggregateRoot
@Entity
@Getter
public class   Account implements Serializable {

	private static final long serialVersionUID = 9023222981284806610L;
	private String name;
	private String lastname;
	private AccountType type;
	private String cpr;
	private AccountId accountId;
	private String bankId;
	Set<Token> tokens = new HashSet<>();

	@JsonbTransient
	private Map<Class<? extends Message>, MessagePassingQueue.Consumer<Message>> handlers = new HashMap<>();


	@Setter(AccessLevel.NONE)
	private List<Event> appliedEvents = new ArrayList<Event>();

	public static Account createAccount(String firstName, String lastName,AccountType type, String cpr,String bankId) {
		var accountID = new AccountId(UUID.randomUUID());
		AccountCreated event = new AccountCreated(accountID, firstName, lastName,type,cpr);
		var account = new Account();
		account.accountId = accountID;
		account.appliedEvents.add(event);
		return account;
	}
	public static Account createFromEvents(Stream<Event> events) {
		Account account = new Account();
		account.applyEvents(events);
		return account;
	}


	public Account() {
		registerEventHandlers();
	}

	public void UpdateAccountTokens(Set<Token> tokens){
		List<Event> events = new ArrayList<>();

		for (Token token: tokens){
			events.add(new AccountTokenAdded(accountId,token));
		}
		appliedEvents.addAll(events);

	}
	public void UpdateBankId(String bankId){


	}

	private void registerEventHandlers() {
		handlers.put(AccountCreated.class, e -> apply((AccountCreated) e));
		handlers.put(AccountTokenAdded.class, e -> apply((AccountTokenAdded) e));
	}



	public void clearAppliedEvents() {
		appliedEvents.clear();
	}

	private void apply(AccountCreated event) {
		accountId = event.getAccountId();
		name = event.getName();
		lastname = event.getLastname();
		cpr = event.getCpr();
		type = event.getType();
	}

	private void apply(AccountTokenAdded event){
		tokens.add(event.getToken());
	}

	private void missingHandler(Message e) {
		throw new Error("handler for event "+e+" missing");
	}

	private void applyEvents(Stream<Event> events) throws Error {
		events.forEachOrdered(e -> {
			this.applyEvent(e);
		});
		if (this.getAccountId() == null) {
			throw new Error("user does not exist");
		}
	}
	private void applyEvent(Event e) {
		handlers.getOrDefault(e.getClass(), this::missingHandler).accept(e);
	}


}
