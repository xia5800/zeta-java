package org.zetaframework.core.redis.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.lang.NonNull;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis帮助类
 *
 * 说明：
 * 为什么叫Helper类，因为util类应该是只具有静态方法的类，不需要new出来也无需注入bean。所以用Helper命名比较好
 *
 * @author WangFan
 * @author gcc
 * @version 1.1 (GitHub文档: https://github.com/whvcse/RedisHelper )
 * @version 1.2 类名变更 util -> helper
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "unchecked"})
public class RedisHelper {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOps;
    private final HashOperations<String, Object, Object> hashOps;
    private final ListOperations<String, Object> listOps;
    private final SetOperations<String, Object> setOps;
    private final ZSetOperations<String, Object> zSetOps;

    public RedisHelper(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        Assert.notNull(redisTemplate, "redisTemplate 为空");
        valueOps = redisTemplate.opsForValue();
        hashOps = redisTemplate.opsForHash();
        listOps = redisTemplate.opsForList();
        setOps = redisTemplate.opsForSet();
        zSetOps = redisTemplate.opsForZSet();
    }


    /** -------------------key相关操作--------------------- */

    /**
     * 修改key的名称
     * @param oldKey String
     * @param newKey String
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当key不存在时才修改key的名称
     * @param oldKey String
     * @param newKey String
     * @return Boolean
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     * @param key String
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     * @param keys Collection<String>
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 是否存在key
     * @param key String
     * @return Boolean
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取key的剩余时间
     * @param key String
     * @return Long
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取key的剩余时间
     * @param key String
     * @return Long
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 设置key的过期时间
     * @param key String
     * @param timeout Long
     * @param unit TimeUnit
     * @return Boolean
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置key的过期时间
     * @param key String
     * @param date Date
     * @return Boolean
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 移除key的过期时间
     * @param key String
     * @return Boolean
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 查找匹配的key
     * @param pattern String
     * @return Set<String>
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 从当前db中随机获取一个key
     * @return String
     */
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 获取key值的类型
     * @param key String
     * @return DataType
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 将当前数据库的key移动到指定的数据库db中
     * @param key String
     * @param dbIndex Int
     * @return Boolean
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 序列化key
     * @param key String
     * @return ByteArray
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * 判断缓存值是否为空对象
     * @param value T
     * @return Boolean
     */
    public <T> Boolean isNullVal(T value) {
        boolean isNull = value == null || NullVal.class.equals(value.getClass());
        return isNull || value.getClass().equals(Object.class) || (value instanceof Map && ((Map<?, ?>) value).isEmpty());
    }

    /**
     * 创建一个空对象
     * @return NullVal
     */
    public NullVal createNullVal() {
        return new NullVal();
    }

    /**
     * 返回正常值或null值
     * @param value T
     * @return T
     */
    public <T> T returnVal(T value) {
        return isNullVal(value) ? null : value;
    }

    /** -------------------string相关操作--------------------- */

    /**
     * 获取指定key的值
     * @param key String
     * @return T
     */
    public <T> T get(String key) {
        return returnVal((T) valueOps.get(key));
    }

    /**
     * 设置指定key的值
     * @param key String
     * @param value Object
     */
    public void set(String key, Object value) {
        value = ObjectUtil.isNull(value) ? createNullVal() : value;
        valueOps.set(key, value);
    }

    /**
     * 若key存在时，为key设置指定的值和过期时间
     * @param key String
     * @param value Object
     * @param timeout Long
     * @param unit TimeUnit
     */
    public void setEx(String key, Object value, Long timeout, TimeUnit unit) {
        value = ObjectUtil.isNull(value) ? createNullVal() : value;
        valueOps.set(key, value, timeout, unit);
    }

    /**
     * 若key不存在时，为key设置指定的值和过期时间
     * @param key String
     * @param value Object
     * @return Boolean
     */
    public Boolean setIfAbsent(String key, Object value) {
        value = ObjectUtil.isNull(value) ? createNullVal() : value;
        return valueOps.setIfAbsent(key, value);
    }

    /**
     * 将给定key的值设为value, 并返回key的旧值old value
     * @param key String
     * @param value Object
     * @return T
     */
    public <T> T getAndSet(String key, Object value) {
        value = ObjectUtil.isNull(value) ? createNullVal() : value;
        return returnVal((T) valueOps.getAndSet(key, value));
    }

    /**
     * 获取字符串的长度
     * @param key String
     * @return Long
     */
    public Long size(String key) {
        return valueOps.size(key);
    }

    /**
     * 批量获取
     * @param keys
     * @return
     */
    public List<Object> multiGet(Collection<String> keys) {
        return valueOps.multiGet(keys);
    }

    /**
     * 批量添加
     * @param maps Map<String, Object>
     */
    public void multiSet(Map<String, Object> maps) {
        valueOps.multiSet(maps);
    }

    /**
     * 同时设置一个或多个key-value对，当且仅当所有给定key都不存在
     * @param maps Map<String, Object>
     * @return Boolean
     */
    public Boolean multiSetIfAbsent(Map<String, Object> maps) {
        return valueOps.multiSetIfAbsent(maps);
    }

    /**
     * 增加(自增长), 负数则为自减
     * @param key
     * @param increment
     * @return
     */
    public Long incrBy(String key, Long increment) {
        return valueOps.increment(key, increment);
    }

    /**
     * 增加(自增长), 负数则为自减
     * @param key
     * @param increment
     * @return
     */
    public Double incrByFloat(String key, Double increment) {
        return valueOps.increment(key, increment);
    }

    /**
     * 追加到末尾
     * @param key
     * @param value
     * @return
     */
    public Integer append(String key, String value) {
        value = StrUtil.isBlank(value) ? StrUtil.EMPTY : value;
        return valueOps.append(key, value);
    }

    /** -------------------hash相关操作-------------------------  */

    /**
     * 获取存储在哈希表中指定字段的值
     * @param key
     * @param field
     * @return
     */
    public <T> T hGet(String key, Object field) {
        return returnVal((T) hashOps.get(key, field));
    }

    /**
     * 获取所有给定字段的值
     * @param key
     * @return
     */
    public Map<Object, Object> hGetAll(String key){
        return hashOps.entries(key);
    }

    /**
     * 获取所有给定字段的值
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return hashOps.multiGet(key, fields);
    }

    /**
     * 将哈希表key中的字段hashKey的值设为value
     * @param key String
     * @param hashKey String
     * @param value Object
     */
    public void hPut(String key, String hashKey, Object value) {
        value = ObjectUtil.isNull(value) ? createNullVal() : value;
        hashOps.put(key, hashKey, value);
    }

    /**
     * 将多个 field-value (字段-值对)设置到哈希表中
     * @param key String
     * @param maps Map<String, Object>
     */
    public void hPutAll(String key, Map<String, Object> maps) {
        hashOps.putAll(key, maps);
    }

    /**
     * 仅当hashKey不存在时才设置
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    public Boolean hPutIfAbsent(String key, String hashKey, Object value) {
        value = ObjectUtil.isNull(value) ? createNullVal() : value;
        return hashOps.putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除一个或多个哈希表字段
     * @param key
     * @param fields
     * @return
     */
    public Long hDelete(String key, Object... fields) {
        return hashOps.delete(key, fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     * @param key
     * @param field
     * @return
     */
    public Boolean hExists(String key, String field) {
        return hashOps.hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     * @param key
     * @param field
     * @param increment
     * @return
     */
    public Long hIncrBy(String key, Object field, Long increment) {
        return hashOps.increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     * @param key
     * @param field
     * @param delta
     * @return
     */
    public Double hIncrByFloat(String key, Object field, Double delta) {
        return hashOps.increment(key, field, delta);
    }

    /**
     * 获取所有哈希表中的字段
     * @param key
     * @return
     */
    public Set<Object> hKeys(String key) {
        return hashOps.keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     * @param key
     * @return
     */
    public Long hSize(String key) {
        return hashOps.size(key);
    }

    /**
     * 获取哈希表中所有值
     * @param key
     * @return
     */
    public List<Object> hValues(String key) {
        return hashOps.values(key);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key
     * @param options
     * @return
     */
    public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return hashOps.scan(key, options);
    }

    /** ------------------------list相关操作----------------------------  */

    /**
     * 通过索引获取列表中的元素
     * @param key
     * @param index
     * @return
     */
    public <T> T lIndex(String key, Long index) {
        return returnVal((T) listOps.index(key, index));
    }

    /**
     * 获取列表指定范围内的元素
     * @param key
     * @param start 开始位置, 0是开始位置
     * @param end 结束位置, -1返回所有
     * @return
     */
    public List<Object> lRange(String key, Long start, Long end) {
        return listOps.range(key, start, end);
    }

    /**
     * 存储在list头部
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPush(String key, Object value) {
        return listOps.leftPush(key, value);
    }

    /**
     * 存储多个值在list头部
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPushAll(String key, Object... value) {
        return listOps.leftPushAll(key, value);
    }

    /**
     * 当list存在的时候才加入
     * @param key
     * @param value
     * @return
     */
    public Long lLeftPushIfPresent(String key, Object value) {
        return listOps.leftPushIfPresent(key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     * @param key
     * @param pivot
     * @param value
     * @return
     */
    public Long lLeftPush(String key, Object pivot, Object value) {
        return listOps.leftPush(key, pivot, value);
    }

    /**
     * 存储多个值在list尾部
     * @param key
     * @param value
     * @return
     */
    public Long lRightPush(String key, Object value) {
        return listOps.rightPush(key, value);
    }

    /**
     * 存储多个值在list尾部
     * @param key
     * @param value
     * @return
     */
    public Long lRightPushAll(String key, Object... value) {
        return listOps.rightPushAll(key, value);
    }

    /**
     * 为已存在的列表添加值
     * @param key
     * @param value
     * @return
     */
    public Long lRightPushIfPresent(String key, Object value) {
        return listOps.rightPushIfPresent(key, value);
    }

    /**
     * 在pivot元素的右边添加值
     * @param key
     * @param pivot
     * @param value
     * @return
     */
    public Long lRightPush(String key, Object pivot, Object value) {
        return listOps.rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     * @param key
     * @param index
     * @param value
     */
    public void lSet(String key, Long index, Object value) {
        listOps.set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     * @param key
     * @return 删除的元素
     */
    public <T> T lLeftPop(String key) {
        return (T) listOps.leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素
     * 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public <T> T lBLeftPop(String key, Long timeout, TimeUnit unit) {
        return (T) listOps.leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     * @param key
     * @return 删除的元素
     */
    public <T> T lRightPop(String key) {
        return (T) listOps.rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素
     * 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public <T> T lBRightPop(String key, Long timeout, TimeUnit unit) {
        return (T) listOps.rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     * @param sourceKey
     * @param destinationKey
     * @return
     */
    public <T> T lRightPopAndLeftPush(@NonNull String sourceKey, String destinationKey) {
        return (T) listOps.rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它
     * 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param sourceKey
     * @param destinationKey
     * @param timeout
     * @param unit
     * @return
     */
    public <T> T lBRightPopAndLeftPush(String sourceKey, String destinationKey, Long timeout, TimeUnit unit) {
        return (T) listOps.rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key
     * @param index
     *          index=0, 删除所有值等于value的元素;
     *          index>0, 从头部开始删除第一个值等于value的元素;
     *          index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value
     * @return
     */
    public Long lRemove(String key, Long index, Object value) {
        return listOps.remove(key, index, value);
    }

    /**
     * 裁剪list
     * @param key
     * @param start
     * @param end
     */
    public void lTrim(String key, Long start, Long end) {
        listOps.trim(key, start, end);
    }

    /**
     * 获取列表长度
     * @param key
     * @return
     */
    public Long lLen(String key) {
        return listOps.size(key);
    }

    /** --------------------set相关操作--------------------------  */
    /**
     * set添加元素
     * @param key
     * @param values
     * @return
     */
    public Long sAdd(String key, Object... values) {
        return setOps.add(key, values);
    }

    /**
     * set移除元素
     * @param key
     * @param values
     * @return
     */
    public Long sRemove(String key, Object... values) {
        return setOps.remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     * @param key
     * @return
     */
    public <T> T sPop(String key) {
        return (T) setOps.pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     * @param key
     * @param value
     * @param destKey
     * @return
     */
    public Boolean sMove(String key, Object value, String destKey) {
        return setOps.move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     * @param key
     * @return
     */
    public Long sSize(String key) {
        return setOps.size(key);
    }

    /**
     * 判断集合是否包含value
     * @param key
     * @param value
     * @return
     */
    public Boolean sIsMember(String key, Object value) {
        return setOps.isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> sIntersect(String key, String otherKey) {
        return setOps.intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sIntersect(String key, Collection<String> otherKeys) {
        return setOps.intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return setOps.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return setOps.intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的并集
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sUnion(String key, String otherKeys) {
        return setOps.union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sUnion(String key, Collection<String> otherKeys) {
        return setOps.union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return setOps.unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return setOps.unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> sDifference(String key, String otherKey) {
        return setOps.difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     * @param key
     * @param otherKeys
     * @return
     */
    public Set<Object> sDifference(String key, Collection<String> otherKeys) {
        return setOps.difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long sDifference(String key, String otherKey, String destKey){
        return setOps.differenceAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long sDifference(String key, Collection<String> otherKeys, String destKey) {
        return setOps.differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回集合 key 中的所有成员。
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        return setOps.members(key);
    }

    /**
     * 随机获取集合中的一个元素
     * @param key
     * @return
     */
    public <T> T sRandomMember(String key) {
        return (T) setOps.randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     * @param key
     * @param count
     * @return
     */
    public List<Object> sRandomMembers(String key, Long count) {
        return setOps.randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     * @param key
     * @param count
     * @return
     */
    public Set<Object> sDistinctRandomMembers(String key, Long count) {
        return setOps.distinctRandomMembers(key, count);
    }


    /**------------------zSet相关操作-------------------------------- */

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, Object value, Double score) {
        return zSetOps.add(key, value, score);
    }

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     * @param key
     * @param scoreMembers
     * @return
     */
    public Long zAdd(String key, Map<Object, Double> scoreMembers) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        scoreMembers.forEach( (score, member) -> {
            tuples.add(new DefaultTypedTuple<>(score, member));
        });
        return zSetOps.add(key, tuples);
    }

    /**
     * 从有序集中删除values。返回已移除元素的数量
     * @param key
     * @param values
     * @return
     */
    public Long zRemove(String key, Object... values) {
        return zSetOps.remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public Double zIncrementScore(String key, Object value, Double delta) {
        return zSetOps.incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     * @param key
     * @param value
     * @return 0表示第一位
     */
    public Long zRank(String key, Object value) {
        return zSetOps.rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     * @param key
     * @param value
     * @return
     */
    public Long zReverseRank(String key, Object value) {
        return zSetOps.reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     * @param key
     * @param start 开始位置
     * @param end 结束位置, -1查询所有
     * @return
     */
    public Set<Object> zRange(String key, Long start, Long end) {
        return zSetOps.range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, Long start, Long end) {
        return zSetOps.rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public Set<Object> zRangeByScore(String key, Double min, Double max) {
        return zSetOps.rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, Double min, Double max) {
        return zSetOps.rangeByScoreWithScores(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key
     * @param min
     * @param max
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, Double min, Double max, Long start, Long end) {
        return zSetOps.rangeByScoreWithScores(key, min, max, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zReverseRange(String key, Long start, Long end) {
        return zSetOps.reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeWithScores(String key, Long start, Long end) {
        return zSetOps.reverseRangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> zReverseRangeByScore(String key, Double min, Double max) {
        return zSetOps.reverseRangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key
     * @param min
     * @param max
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zReverseRangeByScore(String key, Double min, Double max, Long start, Long end) {
        return zSetOps.reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(String key, Double min, Double max) {
        return zSetOps.reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 根据score值获取集合元素数量
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zCount(String key, Double min, Double max) {
        return zSetOps.count(key, min, max);
    }

    /**
     * 获取集合大小
     * @param key
     * @return
     */
    public Long zSize(String key) {
        return zSetOps.size(key);
    }

    /**
     * 获取集合大小
     * @param key
     * @return
     */
    public Long zZCard(String key) {
        return zSetOps.zCard(key);
    }

    /**
     * 获取集合中value元素的score值
     * @param key
     * @param value
     * @return
     */
    public Double zScore(String key, Object value) {
        return zSetOps.score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zRemoveRange(String key, Long start, Long end) {
        return zSetOps.removeRange(key, start, end);
    }

    /**
     * 根据指定的score值的范围来移除成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zRemoveRangeByScore(String key, Double min, Double max) {
        return zSetOps.removeRangeByScore(key, min, max);
    }

    /**
     * 获取key和otherKey的并集并存储在destKey中
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return zSetOps.unionAndStore(key, otherKey, destKey);
    }

    /**
     * 获取key和otherKey的并集并存储在destKey中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return zSetOps.unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 交集
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long zIntersectAndStore(String key, String otherKey, String destKey) {
        return zSetOps.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 交集
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return zSetOps.intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 在key处迭代zset中的元素。
     *
     * @param key
     * @param options
     * @return
     */
    public Cursor<ZSetOperations.TypedTuple<Object>> zScan(String key, ScanOptions options) {
        return zSetOps.scan(key, options);
    }

    /** -------------------限流相关操作--------------------- */
    /**
     * 针对某个key使用lua脚本进行限流
     *
     * 说明：参考<a href="https://www.cnblogs.com/use-D/p/11746299.html">redis lua限流脚本</a>
     * @param key 限流key
     * @param limitPeriod 给定的时间范围 单位(秒)
     * @param limitCount 一定时间内最多访问次数
     */
    public Boolean luaScriptLimit(String key, int limitPeriod, int limitCount) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/limit.lua")));
        redisScript.setResultType(Boolean.class);

        return redisTemplate.execute(redisScript, CollUtil.newArrayList(key), limitPeriod, limitCount);
    }


}
