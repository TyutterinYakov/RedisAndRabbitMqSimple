package rabbit.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	
	
	//Конфиг Jedis клиента для работы с Redis
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		
		RedisStandaloneConfiguration redisConfiguration = 
				new RedisStandaloneConfiguration(host, port);
		
		return new JedisConnectionFactory(redisConfiguration);
	}
	
	//Шаблон для получения доступа к операциям Redis
	@Bean
	public <F, S> RedisTemplate<F, S> redisTemplate(){
		
		final RedisTemplate<F, S> redisTemplate = new RedisTemplate<>();
		
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.afterPropertiesSet();
		
		return redisTemplate;
		
	}
	
	
	
}
