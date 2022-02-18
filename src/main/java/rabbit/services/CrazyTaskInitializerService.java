package rabbit.services;

import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import rabbit.configs.RedisLock;
import rabbit.domains.CrazyTask;
import rabbit.rabbit.CrazyTaskSender;

@Service
public class CrazyTaskInitializerService {

	private static Logger logger = LoggerFactory.getLogger(CrazyTaskInitializerService.class);
	public static final String SERVICE_ID = generateShortId();
	
	private final RedisLock redisLock;
	private final CrazyTaskSender taskSender;
	
	@Autowired
	public CrazyTaskInitializerService(RedisLock redisLock, CrazyTaskSender taskSender) {
		super();
		this.redisLock = redisLock;
		this.taskSender = taskSender;
	}

	
	private static final long ONE_MINUTE_IN_MILLIS = 1000*60;
	private static final String GENERATE_CRAZY_TASKS_KEY = "rabbit:mq:simple:generate:crazy:tasks";
//	@Scheduled(fixedDelay = 8000L)
	@Scheduled(cron="0/5 * * * * *")
	public void generateCrazyTasks() {
		
		if(redisLock.acquireLock(ONE_MINUTE_IN_MILLIS, GENERATE_CRAZY_TASKS_KEY)){
			
			logger.info(Strings.repeat("-", 100));
			logger.info(String.format("Service \"%s\" start generate tasks", SERVICE_ID));
			for(int i =0; i<5; ++i) {
				taskSender.sendCrazyTask(
						new CrazyTask(
								generateShortId(),
								SERVICE_ID
								));
			}
			logger.info(String.format("Service \"%s\" end generate tasks", SERVICE_ID));
			logger.info(Strings.repeat("-", 100));
			
			redisLock.releaseLock(GENERATE_CRAZY_TASKS_KEY);
		}
	}
	
	private static String generateShortId() {
		return UUID.randomUUID().toString().substring(0, 4);
	}
	
	
}
