package com.zx.auth.controller;

import com.zx.common.common.RedisUtil;
import com.zx.common.common.RequestBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Redis控制器
 *
 * @author hmchen
 * @date 2020年2月6日13:50:24
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    // redis中存储的过期时间60s
    private static int ExpireTime = 60;

    @Resource
    private RedisUtil redisUtil;

    @RequestMapping("/set")
    public boolean redisSet(@RequestBody RequestBean requestBean) {
        Map map = (Map) requestBean.getInfo();
        String key = (String) map.get("key");
        String value = (String) map.get("value");
        return redisUtil.set(key, value);
    }

    @RequestMapping("/get")
    public Object redisGet(String key) {
        return redisUtil.get(key);
    }

    @RequestMapping("/expire")
    public boolean expire(String key) {
        return redisUtil.expire(key, ExpireTime);
    }
}
