package account.service.repositories;

import account.service.events.Event;
import message.Message;
import message.implementations.MessageQueueAsync;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

import java.util.function.Consumer;

public class QueueTranslator {
    private MessageQueue messageQueue;

    private MessageQueueAsync localMessageQueue = new MessageQueueAsync();

    public QueueTranslator(MessageQueue queue) {
        this.messageQueue = queue;

    }

    public void addHandler(Class<? extends Message> event, Consumer<Message> handler){
        localMessageQueue.addHandler(event,handler);
        Consumer<messaging.Event> eventConsumer = new Consumer<>() {
            @Override
            public void accept(messaging.Event event1) {
                Message messageEvent = event1.getArgument(0, event);
                handler.accept(messageEvent);

            }
        };
        messageQueue.addHandler(event.getSimpleName(),eventConsumer);

    }

    public void publish(Event m){
        messaging.Event convertedEvent = new messaging.Event(m.getClass().getSimpleName(),new Object[]{m});
        messageQueue.publish(convertedEvent);
        localMessageQueue.publish(m);

    }







}
