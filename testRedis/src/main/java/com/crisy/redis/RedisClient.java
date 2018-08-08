package com.crisy.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClient {
	/**
	 * 一般项目基本都使用非切片池；
	 * 切片池主要用于分布式项目，可以设置主从Redis库
	 */
	private Jedis jedis;//非切片客户端连接
	private JedisPool jedisPool;//非切片连接池
	private ShardedJedis shardedJedis;//切片客户端连接
	private ShardedJedisPool shardedJedisPool;//切片连接池
	
	public RedisClient(){ 
        initialPool(); //初始化非切片连接池
        initialShardedPool(); //初始化切片连接池
        shardedJedis = shardedJedisPool.getResource(); 
        jedis = jedisPool.getResource(); 
    }


	private void initialPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(20);
		config.setMaxIdle(5);
		config.setMaxWait(100001);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config,"127.0.0.1",6379);
	} 
	
	private void initialShardedPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(20);
		config.setMaxIdle(5);
		config.setMaxWait(100001);
		config.setTestOnBorrow(false);
		//slave连接
		List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
		shardInfos.add(new JedisShardInfo("127.0.0.1",6379,"master"));
		//构造池
		shardedJedisPool = new ShardedJedisPool(config, shardInfos);
	}
	
	public void show(){
		System.out.println("----对Value操作的命令----");
		valueOperate();
		System.out.println("----对String操作的命令----");
		stringOperator();
		System.out.println("----对List操作的命令----");
		listOperator();
		System.out.println("----对Set操作的命令----");
		setOperator();
		System.out.println("----对hash操作的命令----");
		hashOperate();
	}
	
	
	/**
	 * 对Value操作的命令
	 * @throws InterruptedException 
	 */
	private void valueOperate(){
		System.out.println("清空当前选择库中所有key："+jedis.flushDB());
		System.out.println("清空所有库中所有key："+jedis.flushAll());
		System.out.println("判断myKey键是否存在："+shardedJedis.exists("myKey")); 
		System.out.println("新增myKey："+shardedJedis.set("myKey","myValue")); 
		System.out.println("判断myKey键是否存在："+shardedJedis.exists("myKey")); 
		System.out.println("查看myKey所储存的值的类型："+jedis.type("myKey"));
		System.out.println("重命名myKey-myKey_1："+jedis.rename("myKey", "myKey_1")); 
		System.out.println("判断myKey键是否存在："+shardedJedis.exists("myKey")); 
		System.out.println("判断myKey_1键是否存在："+shardedJedis.exists("myKey_1")); 
		System.out.println("新增myKey_2："+shardedJedis.set("myKey_2","myValue_2")); 
		System.out.println("查看索引为0的Key："+jedis.select(0));
		System.out.println("当前数据库中key的数目："+jedis.dbSize());
		System.out.println("系统中所有键如下：");
		//返回满足给定pattern的所有key
        Set<String> keys = jedis.keys("*"); 
        Iterator<String> it=keys.iterator() ;   
        while(it.hasNext()){   
            String key = it.next();   
            System.out.println(key);   
        }
        //删除时，如果不存在忽略此命令
        System.out.println("系统中删除myKey_3: "+jedis.del("myKey_3"));
        System.out.println("系统中删除myKey_2: "+jedis.del("myKey_2"));
        System.out.println("判断myKey_2是否存在："+shardedJedis.exists("myKey_2"));
        System.out.println("设置 myKey_1的过期时间为5秒:"+jedis.expire("myKey_1", 5));
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        //没有设置过期时间或者不存在的都返回-1
        System.out.println("获得myKey_1的剩余活动时间："+jedis.ttl("myKey_1"));
        System.out.println("取消设置myKey_1的活动时间："+jedis.persist("myKey_1"));
        System.out.println("查看myKey_1的剩余活动时间："+jedis.ttl("myKey_1"));
	}
	
	/**
	 * 对String操作的命令
	 */
	private void stringOperator(){
		System.out.println("清空当前选择库中所有key："+jedis.flushDB());
		System.out.println("给数据库中名称为key01的string赋予值value："+jedis.set("key01", "value01"));
		System.out.println("给数据库中名称为key02的string赋予值value："+jedis.set("key02", "value02"));
		System.out.println("给数据库中名称为key03的string赋予值value："+jedis.set("key03", "value03"));
		System.out.println("返回数据库中名称为key01的string的value："+jedis.get("key01"));
		System.out.println("返回库中多个string的value："+jedis.mget("key01","key02","key03"));
		System.out.println("添加string，名称为key，值为value："+jedis.setnx("key04","value04"));
		System.out.println("向库中添加string，设定过期时间time："+jedis.setex("key05",5,"value05"));
		System.out.println("批量设置多个string的值："+jedis.mset("key06_1","value06_1","key06_2","value06_2"));
		System.out.println("给数据库中名称为key07的string赋予值value："+jedis.set("key07", "1"));
		System.out.println("名称为key07的string增1操作："+jedis.incr("key07"));
		//ERR value is not an integer or out of range
		//System.out.println("名称为key01的string增1操作："+jedis.incr("key01"));
		System.out.println("名称为key07的string增加integer："+jedis.incrBy("key07", 5));
		System.out.println("名称为key07的string减1操作："+jedis.decr("key07"));
		System.out.println("名称为key07的string减少integer："+jedis.decrBy("key07", 3));
		System.out.println("名称为key的string的值附加value："+jedis.append("key01","Hello World"));
		System.out.println("返回名称为key的string的value的子串："+jedis.substr("key01",0,5));
	}
	
	/**
	 * 对List操作的命令
	 */
	private void listOperator(){
		System.out.println("清空当前选择库中所有key："+jedis.flushDB());
		//准备两个list
		shardedJedis.lpush("mylist", "Java"); 
        shardedJedis.lpush("mylist", "C"); 
        shardedJedis.lpush("mylist", "C++");
        shardedJedis.lpush("mylist", "Java");
		System.out.println("在名称为mylist的list尾添加一个值为value的元素："+jedis.rpush("mylist","phython"));
		System.out.println("在名称为mylist的list头添加一个值为value的 元素："+jedis.rpush("mylist","PHP"));
		System.out.println("返回名称为mylist的list的长度："+jedis.llen("mylist"));
		System.out.println("返回名称为mylist中start至end之间的元素："+jedis.lrange("mylist",0,3));
		System.out.println("截取名称为mylist的list："+jedis.ltrim("mylist",0,3));
		System.out.println("返回名称为mylist的list中index位置的元素："+jedis.lindex("mylist",0));
		System.out.println("给名称为mylist的list中index位置的元素赋值："+jedis.lset("mylist",0,"hahahahah我改啦"));
		System.out.println("删除count个mylist的key中值为value的元素："+jedis.lrem("mylist",1,"Java"));
		System.out.println("返回并删除名称为mylist的list中的首元素："+jedis.lpop("mylist"));
		System.out.println("返回并删除名称为mylist的list中的尾元素："+jedis.rpop("mylist"));
	}
	
	/**
	 * 对Set操作的命令
	 */
	private void setOperator(){
		System.out.println("清空当前选择库中所有key："+jedis.flushDB());
		//准备两个set
		jedis.sadd("myset","hahaha");
		jedis.sadd("myset","hahaha");
		jedis.sadd("myset","lalala");
		jedis.sadd("myset","kakaka");
		jedis.sadd("myset2","hahaha");
		jedis.sadd("myset2","lalala");
		jedis.sadd("myset2","kakaka");
		System.out.println("向名称为myset的set中添加元素member："+jedis.sadd("myset","wawawa"));
		System.out.println("返回名称为myset的set的所有元素："+jedis.smembers("myset"));
		System.out.println("删除名称为myset的set中的元素member："+jedis.srem("myset","wawawa"));
		System.out.println("返回名称为myset的set的所有元素："+jedis.smembers("myset"));
		System.out.println("随机返回并删除名称为myset的set中一个元素："+jedis.spop("myset"));
		System.out.println("移到集合元素："+jedis.smove("myset","myset2","hahaha"));
		System.out.println("返回名称为myset的set的所有元素："+jedis.smembers("myset"));
		System.out.println("返回名称为myset2的set的所有元素："+jedis.smembers("myset2"));
		System.out.println("返回名称为mykey的set的基数："+jedis.scard("myset"));
		System.out.println("member是否是名称为mykey的set的元素："+jedis.sismember("myset","hahaha"));
		System.out.println("求交集："+jedis.sinter("myset","myset2"));
		System.out.println("求交集并将交集保存到dstkey的集合："+jedis.sinterstore("dstkey","myset2","myset"));
		System.out.println("返回名称为dstkey的set的所有元素："+jedis.smembers("dstkey"));
		System.out.println("求并集："+jedis.sunion("myset","myset2"));
		System.out.println("求并集并将并集保存到dstkey的集合："+jedis.sinterstore("dstkey","myset2","myset"));
		System.out.println("返回名称为dstkey的set的所有元素："+jedis.smembers("dstkey"));
		System.out.println("求差集："+jedis.sdiff("myset2","myset"));
		System.out.println("求差集并将差集保存到dstkey的集合："+jedis.sdiffstore("dstkey","myset2","myset"));
		System.out.println("返回名称为dstkey的set的所有元素："+jedis.smembers("dstkey"));
		System.out.println("随机返回名称为dstkey的set的一个元素："+jedis.srandmember("dstkey"));
	}
	
	/**
	 * hash
	 */
	private void hashOperate(){
		System.out.println(jedis.flushDB());
		System.out.println("向名称为key的hash中添加元素field1："+shardedJedis.hset("key", "field1", "value1")); 
        System.out.println("向名称为key的hash中添加元素field2："+shardedJedis.hset("key", "field2", "value2")); 
        System.out.println("向名称为key的hash中添加元素field3："+shardedJedis.hset("key", "field3", "value3"));
        System.out.println("返回名称为key的hash中field1对应的value："+shardedJedis.hget("key", "field1"));
        System.out.println("返回名称为key的hash中field1对应的value："+shardedJedis.hmget("key", "field1","field2","field3"));
        System.out.println("删除名称为key的hash中键为field1的域："+shardedJedis.hdel("key", "field1"));
        System.out.println("返回名称为key的hash中所有键对应的value："+shardedJedis.hvals("key"));
        System.out.println("返回名称为key的hash中元素个数："+shardedJedis.hlen("key"));
        System.out.println("返回名称为key的hash中所有键："+shardedJedis.hkeys("key"));
        System.out.println("返回名称为key的hash中所有的键（field）及其对应的value："+shardedJedis.hgetAll("key"));
        System.out.println("名称为key的hash中是否存在键为field1的域："+shardedJedis.hexists("key","field1"));
	}
}