package account.service.rest.adapter;

import account.service.repositories.AccountReadRepo;
import account.service.repositories.AccountRepo;
import account.service.service.AccountService;
import messaging.MessageQueue;
import messaging.implementations.MessageQueueAsync;
import messaging.implementations.RabbitMqQueue;

public class AccountServiceFactory {
	static AccountService service = null;
	static AccountReadRepo accountReadRepo = null;

	static AccountRepo accountRepo = null;
	private final MessageQueue mq =  new RabbitMqQueue("rabbitMq","event");


	public synchronized AccountService getService(){
		if (service != null) {
			return service;
		}
		if(accountReadRepo == null)
		{
			accountReadRepo = new AccountReadRepo(mq);
		}if(accountRepo == null)
		{
			accountRepo = new AccountRepo(mq);
		}
		service = new AccountService(mq,accountRepo,accountReadRepo);
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
