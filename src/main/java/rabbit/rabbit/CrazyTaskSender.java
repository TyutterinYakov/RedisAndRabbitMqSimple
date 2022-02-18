package rabbit.rabbit;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

import rabbit.domains.CrazyTask;

@Component
public class CrazyTaskSender {

	private final RabbitMessagingTemplate rabbitMessaging;

	public CrazyTaskSender(RabbitMessagingTemplate rabbitMessaging) {
		super();
		this.rabbitMessaging = rabbitMessaging;
	}
	
	
	public void sendCrazyTask(CrazyTask task) {
		
		rabbitMessaging.convertAndSend(CrazyTaskListener.CRAZY_TASKS_EXCHANGE, null, task);
	}
}
