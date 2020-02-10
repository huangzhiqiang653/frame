package com.zx.common.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * redisSession配置类
 * 用于指定sess的过期时间，默认是30分钟，可以按系统的实际情况改写maxInactiveIntervalInSeconds，设置session过期时间。
 *
 * @author hmchen
 * @date 2020年2月9日15:09:21
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {
}
