package account.service.repositories;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import account.service.aggregate.Token;
import account.service.events.AccountDeleted;
import messaging.MessageQueue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepo {

    AccountEventStore eventStore;

    public AccountRepo(MessageQueue bus) {
        this.eventStore = new AccountEventStore(bus);
    }



    public void save(Account account){
        eventStore.addEvents(account.getAccountId(),account.getAppliedEvents());
        account.clearAppliedEvents();
    }


    public Account getAccount(AccountId accountId) {
        return Account.createFromEvents(eventStore.getEventsFor(accountId));
    }
    public void deleteAccount(AccountId accountId) {
       eventStore.addEvent(accountId,new AccountDeleted(accountId));
    }

    public List<Token> getTokens(AccountId accountId) {
        return Account.createFromEvents(eventStore.getEventsFor(accountId)).getTokens().stream().toList();
    }
}
