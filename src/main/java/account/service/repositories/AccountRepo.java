package account.service.repositories;

import account.service.aggregate.Account;
import account.service.aggregate.AccountId;
import messaging.MessageQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepo {
    Map<AccountId, Account> accountIdToAccountMap = new ConcurrentHashMap<>();
    Map<AccountId, String> cprToAccountIdMap = new ConcurrentHashMap<>();
    Map<AccountId,CompletableFuture<Account>> accountRequestMap = new ConcurrentHashMap<>();

    AccountEventStore eventStore;

    public AccountRepo(MessageQueue bus) {
        this.eventStore = new AccountEventStore(bus);
    }

    public void storeAccount(Account account) {
        accountIdToAccountMap.put(account.getAccountId(), account);
        cprToAccountIdMap.put(account.getAccountId(), account.getCpr());
    }

    public void save(Account account){
        eventStore.addEvents(account.getAccountId(),account.getAppliedEvents());
        account.clearAppliedEvents();

    }


    public Account getAccount(AccountId accountId) {
        return Account.createFromEvents(eventStore.getEventsFor(accountId));
    }
    public void deleteAccount(AccountId accountId) {
        Account a = accountIdToAccountMap.get(accountId);
        if(a==null){
            return;
        }
        cprToAccountIdMap.remove(a.getCpr());
        accountIdToAccountMap.remove(accountId);
    }

    public boolean accountExists(String cpr) {
        return cprToAccountIdMap.containsKey(cpr);
    }

    public void storeAccountRequest(String newAccountNumber, CompletableFuture<Account> registeredAccount) {
       // accountRequestMap.put(newAccountNumber,registeredAccount);
    }
    public CompletableFuture<Account> getAccountRequest(String accountId) {
        return accountRequestMap.get(accountId);
    }
}
