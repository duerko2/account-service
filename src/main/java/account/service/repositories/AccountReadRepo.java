package account.service.repositories;

import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.events.AccountCreated;
import account.service.events.AccountDeleted;
import messaging.MessageQueue;
import org.jmolecules.ddd.annotation.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountReadRepo {

    private Map<AccountId, String> cprSet = new HashMap<>();
    private Map<AccountId, String> bankIds = new HashMap<>();

    private Map<AccountId, AccountType> accountTypes = new HashMap<>();


    public AccountReadRepo(MessageQueue eventQueue) {
        eventQueue.addHandler(AccountCreated.class, e -> {
            apply((AccountCreated) e);
        });

        eventQueue.addHandler(AccountDeleted.class, e-> apply((AccountDeleted) e));

    }

    public boolean accountExists(String cpr){
        return cprSet.containsValue(cpr);
    }


    public void apply(AccountCreated event) {
        cprSet.put(event.getAccountId(),event.getCpr());
        accountTypes.put(event.getAccountId(),event.getType());
    }

    public void apply(AccountDeleted event){
        cprSet.remove(event.getAccountId());
        accountTypes.remove(event.getAccountId());
        bankIds.remove(event.getAccountId());

    }
}
