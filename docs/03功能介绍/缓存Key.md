# 缓存key
本项目之前的redis使用方式很原始，每次都需要构造缓存key再进行相关的查询、修改操作。

例如：
```java
// 获取验证码缓存值
public String getVerifyCode(Param param) {
    // 构造缓存key
    String captchaKey = StrUtil.format("{}:{}", SystemRedisKeyConstants.CAPTCHA_KEY, param.getKey());
    // 通过缓存key获取缓存值
    String verifyCode = redisUtil.get(captchaKey); 
    return verifyCode;
}
```

这是不太优雅的，于是参考了lamp-boot项目的实现之后，自己封装了一个优雅的缓存使用方式。


## 编写一个自定义的缓存key

编写一个自定义缓存很简单，只要实现相应的接口`StringCacheKey`、`ListCacheKye`，重写相应的方法即可

例如：业务需要一个String类型的redis缓存，这个缓存的过期时间为5分钟，这个缓存的key前缀为:`system:captcha`

代码实现如下：

CaptchaStringCacheKey.java

```java
import com.zeta.common.constants.SystemRedisKeyConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.zetaframework.core.redis.model.StringCacheKey;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class CaptchaStringCacheKey implements StringCacheKey {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * key 前缀
     */
    @Override
    public String getPrefix() {
        return SystemRedisKeyConstants.CAPTCHA_KEY;
    }

    /**
     * key 过期时间
     */
    @Override
    public Duration getExpire() {
        // 5分钟
        return Duration.ofMinutes(5);
    }

    /**
     * 获取 redisTemplate
     */
    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

}
```

## 如何使用？

下面只例举了简单使用方法，更加详细的使用方式请查看`CaptchaStringCacheKey`、`StringCacheKey`的注释

```java
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.zeta.common.cacheKey.CaptchaStringCacheKey;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final CaptchaStringCacheKey captchaCacheKey;
    
    @GetMapping
    public void example() {
        // 设置缓存
        captchaCacheKey.set("123", "验证码的值");
        
        // 获取缓存值
        String cacheValue = captchaCacheKey.get("123");
        System.out.println(cacheValue);
        
        // 删除缓存
        captchaCacheKey.delete("123");
    }
    
}
```

以上demo涉及到的类有：
```
/** 业务包 */
// 验证码缓存key
com.zeta.common.cacheKey.CaptchaStringCacheKey

/** zetaframework包 */
// 缓存key
org.zetaframework.core.redis.model.CacheKey.kt
// Hash(Map)类型的缓存key
org.zetaframework.core.redis.model.HashCacheKey
// List类型的缓存key
org.zetaframework.core.redis.model.ListCacheKey
// Set类型的缓存key
org.zetaframework.core.redis.model.SetCacheKey
// String类型的缓存key
org.zetaframework.core.redis.model.StringCacheKey
// ZSet类型的缓存key
org.zetaframework.core.redis.model.ZSetCacheKey
```
