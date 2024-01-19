package behaviourtests;

import message.Message;
import message.MessageQueue;

import java.util.function.Consumer;

public class MessageQueMock implements MessageQueue {
    @Override
    public void publish(Message message) {

    }

    @Override
    public void addHandler(Class<? extends Message> event, Consumer<Message> handler) {

    }
}
