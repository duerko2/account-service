package account.service.rest.adapter;

import account.service.AccountService;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

/**
 * @Author: Marcus Jacobsen
 * Pair programming with Alexander Elsing
 * Based on code from Hubert Baumeister
 */
public class AccountServiceFactory {
	static AccountService service = null;

	private final MessageQueue mq = new RabbitMqQueue("rabbitMq");


	public synchronized AccountService getService(){
		if (service != null) {
			return service;
		}
		service = new AccountService(mq);
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
