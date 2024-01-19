package account.service.repositories;

import account.service.aggregate.AccountId;
import account.service.events.*;

import lombok.NonNull;
import message.MessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AccountEventStore {
    private Map<UUID, List<Event>> store = new ConcurrentHashMap<>();

    private MessageQueue eventBus;

    public AccountEventStore(MessageQueue eventBus) {
        this.eventBus = eventBus;
    }

    public void addEvent(AccountId id, Event event) {

        if (!store.containsKey(event.getAccountId().getUuid())) {
            store.put(id.getUuid(), new ArrayList<Event>());
        }
        store.get(id.getUuid()).add(event);
        eventBus.publish(event);
    }
    public Stream<Event> getEventsFor(AccountId id) {
        if (!store.containsKey(id.getUuid())) {
            store.put(id.getUuid(), new ArrayList<Event>());
        }
        return store.get(id.getUuid()).stream();
    }

    public void deleteAccount(AccountId accountId){
        store.remove(accountId.getUuid());
    }
    public void addEvents(@NonNull AccountId accountId, List<Event> appliedEvents) {
        appliedEvents.stream().forEach(e -> addEvent(accountId, e));
    }
}
