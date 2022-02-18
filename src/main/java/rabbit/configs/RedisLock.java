package rabbit.configs;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLock {

	private final RedisTemplate<String, Long> redisTemplate;
	
	public RedisLock(RedisTemplate<String, Long> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}


	private static final String KEY_TEMPLATE = "lock:%s";
	
	
	/**
	 * Метод запроса блокировки.
	 * @param expireMillis - время, на которое задача будет заблокирована в мл/с
	 * @param taskKey - ключ, по которрому будет заблокирована задача 
	 * @param статус захвата (захвачена / не захвачена)
	 */
	public boolean acquireLock(long expireMillis, String taskKey) {
		
		//Поулчаем ключ блокировки
		String lockKey = getLockKey(taskKey);
		
		//Получаем время истечения этой блокировки
		Long expireAt = redisTemplate.opsForValue().get(lockKey);
		
		long currentTimeMillis = System.currentTimeMillis();
		
		//Если блокировка есть
		if(Objects.nonNull(expireAt)) {
			
			//И она истекла, удаляем ее
			if(expireAt<=currentTimeMillis) {
				redisTemplate.delete(lockKey);
			} else {
				return false;
			}
		}
		
		//Иначе создаем блокировку и рибавляем к текущему времени, на которое блокируется задача
		Long expire = currentTimeMillis + expireMillis;
		
		return Optional
				.ofNullable(redisTemplate.opsForValue().setIfAbsent(lockKey, expire))
				.orElse(false);
		
	}
	
	/**
	 * Метод освобождения блокировки
	 */
	public void releaseLock(String taskKey) {
		String lockKey = getLockKey(taskKey);
		
		redisTemplate.delete(lockKey);
		
	}
	
	private String getLockKey(String key) {
		return String.format(KEY_TEMPLATE, key);
	}
}
