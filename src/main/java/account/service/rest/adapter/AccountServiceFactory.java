package account.service.rest.adapter;

import account.service.repositories.AccountReadRepo;
import account.service.repositories.AccountRepo;
import account.service.repositories.QueueTranslator;
import account.service.service.AccountService;
import message.implementations.MessageQueueAsync;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import message.implementations.MessageQueueAsync;

/**
 * @Author: Marcus Jacobsen
 * Pair programming with Alexander Elsing
 * Based on code from Hubert Baumeister
 */
public class AccountServiceFactory {
	static AccountService service = null;
	static AccountReadRepo accountReadRepo = null;

	static AccountRepo accountRepo = null;

	private final MessageQueue mq =  new RabbitMqQueue("rabbitMq");
	private final message.MessageQueue localQue = new MessageQueueAsync();


	public synchronized AccountService getService(){
		if (service != null) {
			return service;
		}

		if(accountReadRepo == null)
		{
			accountReadRepo = new AccountReadRepo(localQue);
		}if(accountRepo == null)
		{
			accountRepo = new AccountRepo(localQue);
		}
		service = new AccountService(localQue,mq,accountRepo,accountReadRepo);
		System.out.printf("RabbitMQ created");
		return service;
	}

	/*
	public synchronized StudentRegistrationService getService() {
		// The singleton pattern.
		// Ensure that there is at most
		// one instance of a PaymentService
		if (service != null) {
			return service;
		}
		
		// Hookup the classes to send and receive
		// messages via RabbitMq, i.e. RabbitMqSender and
		// RabbitMqListener. 
		// This should be done in the factory to avoid 
		// the PaymentService knowing about them. This
		// is called dependency injection.
		// At the end, we can use the PaymentService in tests
		// without sending actual messages to RabbitMq.
		var mq = new RabbitMqQueue("rabbitMq");
		service = new StudentRegistrationService(mq);
//		new StudentRegistrationServiceAdapter(service, mq);
		return service;
	}

	 */
}
