package behaviourtests;

import messaging.Message;
import messaging.MessageQueue;

import java.util.function.Consumer;

public class MessageQueMock implements MessageQueue {
    @Override
    public void publish(Message message) {

    }

    @Override
    public void addHandler(Class<? extends Message> event, Consumer<Message> handler) {

    }
}
