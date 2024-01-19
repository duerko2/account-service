package account.service.repositories;

import account.service.aggregate.AccountId;
import account.service.events.*;

import lombok.NonNull;
import messaging.MessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AccountEventStore {
    private Map<AccountId, List<Event>> store = new ConcurrentHashMap<>();

    private MessageQueue eventBus;

    public AccountEventStore(MessageQueue eventBus) {
        this.eventBus = eventBus;
    }

    public void addEvent(AccountId id, Event event) {

        if (!store.containsKey(event.getAccountId())) {
            store.put(event.getAccountId(), new ArrayList<Event>());
        }
        store.get(event.getAccountId()).add(event);
        eventBus.publish(event);
    }
    public Stream<Event> getEventsFor(AccountId id) {
        if (!store.containsKey(id)) {
            store.put(id, new ArrayList<Event>());
        }
        return store.get(id).stream();
    }

    public void addEvents(@NonNull AccountId accountId, List<Event> appliedEvents) {
        appliedEvents.stream().forEach(e -> addEvent(accountId, e));
    }
}
