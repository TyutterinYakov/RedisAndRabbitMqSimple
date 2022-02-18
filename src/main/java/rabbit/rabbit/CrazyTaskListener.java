package rabbit.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rabbit.domains.CrazyTask;
import rabbit.services.CrazyTaskInitializerService;

@Component
public class CrazyTaskListener {

	private static final Logger logger = LoggerFactory.getLogger(CrazyTaskListener.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	public static final String CRAZY_TASKS_QUEUE = "rebbit.mq.simple.queue";
	public static final String CRAZY_TASKS_EXCHANGE = "rebbit.mq.simple.exchange";
	
	//Прием задачи
	@RabbitListener(
			bindings = @QueueBinding(
					value = @Queue(value = CRAZY_TASKS_QUEUE),
					exchange = @Exchange(value=CRAZY_TASKS_EXCHANGE)
			)
	)
	public void handleCrazyTask(CrazyTask task) throws JsonProcessingException, InterruptedException {
//		Thread.sleep(15_000);
		logger.info(
				String.format(
						"Service \"%s\" end process task \"%s\" from service \"%s\"",
						CrazyTaskInitializerService.SERVICE_ID,
						task.getId(),
						task.getFromServer()
				)
		);
		logger.info(objectMapper.writeValueAsString(task));
	}
	
	
}
