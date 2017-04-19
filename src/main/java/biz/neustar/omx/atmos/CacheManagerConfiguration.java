package biz.neustar.omx.atmos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;

/**
 * The Class CacheManagerConfiguration.
 */
public class CacheManagerConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(CacheManagerConfiguration.class);

	/** The redis template. */

	@Value("${redis.hosts}")
	private String redisHostsProperty;

	@Value("${redis.master}")
	private String redisMaster;

	@Value("${redis.password}")
	private String password;

	@Autowired
	JedisPoolConfig jedisPoolConfig;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Bean
	CacheManager cacheManager() {
		return new RedisCacheManager(redisTemplate);
	}

	@Bean(name = { "jedisConnFactory" })
	public JedisConnectionFactory connectionFactory() {

		List<RedisHost> redisHostList = getRedisHosts();

		JedisConnectionFactory jedisConnFactory = null;
		if (!CollectionUtils.isEmpty(redisHostList)) {
			// if sentinel configuration not found , switching to standalone
			// mode.
			if (redisHostList.size() > 1) {
				jedisConnFactory = new JedisConnectionFactory(getSentinelConfiguration(redisHostList));
			} else {
				RedisHost redisHost = redisHostList.get(0);
				jedisConnFactory = new JedisConnectionFactory();
				jedisConnFactory.setHostName(redisHost.getHostName());
				jedisConnFactory.setPort(redisHost.getPort());
			}

			jedisConnFactory.setPoolConfig(jedisPoolConfig);
			jedisConnFactory.setPassword(password);
		}

		return jedisConnFactory;
	}

	public RedisSentinelConfiguration getSentinelConfiguration(List<RedisHost> redisHostList) {
		RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration().master(redisMaster);
		for (RedisHost redisHost : redisHostList) {
			redisSentinelConfiguration.sentinel(redisHost.getHostName(), redisHost.getPort());
		}

		return redisSentinelConfiguration;
	}

	private List<RedisHost> getRedisHosts() {
		List<RedisHost> redisHostList = new ArrayList<>();
		try {
			if (!StringUtils.isEmpty(redisHostsProperty)) {
				String[] hostsList = redisHostsProperty.split(",");
				redisHostList = getRedisHostList(hostsList);
			}
		} catch (Exception e) {
			LOG.error("Exception while getting the details of each redis host from the property", e);
			redisHostList.clear();
		}
		return redisHostList;
	}

	private List<RedisHost> getRedisHostList(String[] hostsList) {
		List<RedisHost> redisHostList = new ArrayList<>();
		RedisHost redisHost;
		for (String hosts : hostsList) {
			if (!StringUtils.isEmpty(hosts)) {
				String[] urlList = hosts.split(":");
				if (urlList != null && urlList.length > 1) {
					redisHost = new RedisHost();
					redisHost.setHostName(urlList[0]);
					redisHost.setPort(Integer.parseInt(urlList[1]));
					redisHostList.add(redisHost);
				}
			}
		}
		return redisHostList;
	}
}
