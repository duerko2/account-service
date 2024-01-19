package account.service.repositories;

import account.service.aggregate.AccountId;
import account.service.aggregate.AccountType;
import account.service.events.AccountCreated;
import account.service.events.AccountDeleted;
import account.service.events.AccountTokenAdded;
import message.MessageQueue;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountReadRepo {

    private Map<AccountId, String> cprSet = new ConcurrentHashMap<>();
    private Map<AccountId, String> bankIds = new ConcurrentHashMap<>();

    private Map<AccountId, AccountType> accountTypes = new ConcurrentHashMap<>();


    public AccountReadRepo(message.MessageQueue eventQueue) {
        eventQueue.addHandler(AccountCreated.class, e -> {
            apply((AccountCreated) e);
        });

        eventQueue.addHandler(AccountDeleted.class, e-> apply((AccountDeleted) e));

    }

    public boolean accountExists(String cpr){
        // print all cpr in the map
        cprSet.values().forEach(System.out::println);
        System.out.println("The cpr we are looking for: " + cpr);
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
