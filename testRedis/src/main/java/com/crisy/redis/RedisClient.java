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
	 * һ����Ŀ������ʹ�÷���Ƭ�أ�
	 * ��Ƭ����Ҫ���ڷֲ�ʽ��Ŀ��������������Redis��
	 */
	private Jedis jedis;//����Ƭ�ͻ�������
	private JedisPool jedisPool;//����Ƭ���ӳ�
	private ShardedJedis shardedJedis;//��Ƭ�ͻ�������
	private ShardedJedisPool shardedJedisPool;//��Ƭ���ӳ�
	
	public RedisClient(){ 
        initialPool(); //��ʼ������Ƭ���ӳ�
        initialShardedPool(); //��ʼ����Ƭ���ӳ�
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
		//slave����
		List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
		shardInfos.add(new JedisShardInfo("127.0.0.1",6379,"master"));
		//�����
		shardedJedisPool = new ShardedJedisPool(config, shardInfos);
	}
	
	public void show(){
		System.out.println("----��Value����������----");
		valueOperate();
		System.out.println("----��String����������----");
		stringOperator();
		System.out.println("----��List����������----");
		listOperator();
		System.out.println("----��Set����������----");
		setOperator();
		System.out.println("----��hash����������----");
		hashOperate();
	}
	
	
	/**
	 * ��Value����������
	 * @throws InterruptedException 
	 */
	private void valueOperate(){
		System.out.println("��յ�ǰѡ���������key��"+jedis.flushDB());
		System.out.println("������п�������key��"+jedis.flushAll());
		System.out.println("�ж�myKey���Ƿ���ڣ�"+shardedJedis.exists("myKey")); 
		System.out.println("����myKey��"+shardedJedis.set("myKey","myValue")); 
		System.out.println("�ж�myKey���Ƿ���ڣ�"+shardedJedis.exists("myKey")); 
		System.out.println("�鿴myKey�������ֵ�����ͣ�"+jedis.type("myKey"));
		System.out.println("������myKey-myKey_1��"+jedis.rename("myKey", "myKey_1")); 
		System.out.println("�ж�myKey���Ƿ���ڣ�"+shardedJedis.exists("myKey")); 
		System.out.println("�ж�myKey_1���Ƿ���ڣ�"+shardedJedis.exists("myKey_1")); 
		System.out.println("����myKey_2��"+shardedJedis.set("myKey_2","myValue_2")); 
		System.out.println("�鿴����Ϊ0��Key��"+jedis.select(0));
		System.out.println("��ǰ���ݿ���key����Ŀ��"+jedis.dbSize());
		System.out.println("ϵͳ�����м����£�");
		//�����������pattern������key
        Set<String> keys = jedis.keys("*"); 
        Iterator<String> it=keys.iterator() ;   
        while(it.hasNext()){   
            String key = it.next();   
            System.out.println(key);   
        }
        //ɾ��ʱ����������ں��Դ�����
        System.out.println("ϵͳ��ɾ��myKey_3: "+jedis.del("myKey_3"));
        System.out.println("ϵͳ��ɾ��myKey_2: "+jedis.del("myKey_2"));
        System.out.println("�ж�myKey_2�Ƿ���ڣ�"+shardedJedis.exists("myKey_2"));
        System.out.println("���� myKey_1�Ĺ���ʱ��Ϊ5��:"+jedis.expire("myKey_1", 5));
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        //û�����ù���ʱ����߲����ڵĶ�����-1
        System.out.println("���myKey_1��ʣ��ʱ�䣺"+jedis.ttl("myKey_1"));
        System.out.println("ȡ������myKey_1�Ļʱ�䣺"+jedis.persist("myKey_1"));
        System.out.println("�鿴myKey_1��ʣ��ʱ�䣺"+jedis.ttl("myKey_1"));
	}
	
	/**
	 * ��String����������
	 */
	private void stringOperator(){
		System.out.println("��յ�ǰѡ���������key��"+jedis.flushDB());
		System.out.println("�����ݿ�������Ϊkey01��string����ֵvalue��"+jedis.set("key01", "value01"));
		System.out.println("�����ݿ�������Ϊkey02��string����ֵvalue��"+jedis.set("key02", "value02"));
		System.out.println("�����ݿ�������Ϊkey03��string����ֵvalue��"+jedis.set("key03", "value03"));
		System.out.println("�������ݿ�������Ϊkey01��string��value��"+jedis.get("key01"));
		System.out.println("���ؿ��ж��string��value��"+jedis.mget("key01","key02","key03"));
		System.out.println("���string������Ϊkey��ֵΪvalue��"+jedis.setnx("key04","value04"));
		System.out.println("��������string���趨����ʱ��time��"+jedis.setex("key05",5,"value05"));
		System.out.println("�������ö��string��ֵ��"+jedis.mset("key06_1","value06_1","key06_2","value06_2"));
		System.out.println("�����ݿ�������Ϊkey07��string����ֵvalue��"+jedis.set("key07", "1"));
		System.out.println("����Ϊkey07��string��1������"+jedis.incr("key07"));
		//ERR value is not an integer or out of range
		//System.out.println("����Ϊkey01��string��1������"+jedis.incr("key01"));
		System.out.println("����Ϊkey07��string����integer��"+jedis.incrBy("key07", 5));
		System.out.println("����Ϊkey07��string��1������"+jedis.decr("key07"));
		System.out.println("����Ϊkey07��string����integer��"+jedis.decrBy("key07", 3));
		System.out.println("����Ϊkey��string��ֵ����value��"+jedis.append("key01","Hello World"));
		System.out.println("��������Ϊkey��string��value���Ӵ���"+jedis.substr("key01",0,5));
	}
	
	/**
	 * ��List����������
	 */
	private void listOperator(){
		System.out.println("��յ�ǰѡ���������key��"+jedis.flushDB());
		//׼������list
		shardedJedis.lpush("mylist", "Java"); 
        shardedJedis.lpush("mylist", "C"); 
        shardedJedis.lpush("mylist", "C++");
        shardedJedis.lpush("mylist", "Java");
		System.out.println("������Ϊmylist��listβ���һ��ֵΪvalue��Ԫ�أ�"+jedis.rpush("mylist","phython"));
		System.out.println("������Ϊmylist��listͷ���һ��ֵΪvalue�� Ԫ�أ�"+jedis.rpush("mylist","PHP"));
		System.out.println("��������Ϊmylist��list�ĳ��ȣ�"+jedis.llen("mylist"));
		System.out.println("��������Ϊmylist��start��end֮���Ԫ�أ�"+jedis.lrange("mylist",0,3));
		System.out.println("��ȡ����Ϊmylist��list��"+jedis.ltrim("mylist",0,3));
		System.out.println("��������Ϊmylist��list��indexλ�õ�Ԫ�أ�"+jedis.lindex("mylist",0));
		System.out.println("������Ϊmylist��list��indexλ�õ�Ԫ�ظ�ֵ��"+jedis.lset("mylist",0,"hahahahah�Ҹ���"));
		System.out.println("ɾ��count��mylist��key��ֵΪvalue��Ԫ�أ�"+jedis.lrem("mylist",1,"Java"));
		System.out.println("���ز�ɾ������Ϊmylist��list�е���Ԫ�أ�"+jedis.lpop("mylist"));
		System.out.println("���ز�ɾ������Ϊmylist��list�е�βԪ�أ�"+jedis.rpop("mylist"));
	}
	
	/**
	 * ��Set����������
	 */
	private void setOperator(){
		System.out.println("��յ�ǰѡ���������key��"+jedis.flushDB());
		//׼������set
		jedis.sadd("myset","hahaha");
		jedis.sadd("myset","hahaha");
		jedis.sadd("myset","lalala");
		jedis.sadd("myset","kakaka");
		jedis.sadd("myset2","hahaha");
		jedis.sadd("myset2","lalala");
		jedis.sadd("myset2","kakaka");
		System.out.println("������Ϊmyset��set�����Ԫ��member��"+jedis.sadd("myset","wawawa"));
		System.out.println("��������Ϊmyset��set������Ԫ�أ�"+jedis.smembers("myset"));
		System.out.println("ɾ������Ϊmyset��set�е�Ԫ��member��"+jedis.srem("myset","wawawa"));
		System.out.println("��������Ϊmyset��set������Ԫ�أ�"+jedis.smembers("myset"));
		System.out.println("������ز�ɾ������Ϊmyset��set��һ��Ԫ�أ�"+jedis.spop("myset"));
		System.out.println("�Ƶ�����Ԫ�أ�"+jedis.smove("myset","myset2","hahaha"));
		System.out.println("��������Ϊmyset��set������Ԫ�أ�"+jedis.smembers("myset"));
		System.out.println("��������Ϊmyset2��set������Ԫ�أ�"+jedis.smembers("myset2"));
		System.out.println("��������Ϊmykey��set�Ļ�����"+jedis.scard("myset"));
		System.out.println("member�Ƿ�������Ϊmykey��set��Ԫ�أ�"+jedis.sismember("myset","hahaha"));
		System.out.println("�󽻼���"+jedis.sinter("myset","myset2"));
		System.out.println("�󽻼������������浽dstkey�ļ��ϣ�"+jedis.sinterstore("dstkey","myset2","myset"));
		System.out.println("��������Ϊdstkey��set������Ԫ�أ�"+jedis.smembers("dstkey"));
		System.out.println("�󲢼���"+jedis.sunion("myset","myset2"));
		System.out.println("�󲢼������������浽dstkey�ļ��ϣ�"+jedis.sinterstore("dstkey","myset2","myset"));
		System.out.println("��������Ϊdstkey��set������Ԫ�أ�"+jedis.smembers("dstkey"));
		System.out.println("����"+jedis.sdiff("myset2","myset"));
		System.out.println("����������浽dstkey�ļ��ϣ�"+jedis.sdiffstore("dstkey","myset2","myset"));
		System.out.println("��������Ϊdstkey��set������Ԫ�أ�"+jedis.smembers("dstkey"));
		System.out.println("�����������Ϊdstkey��set��һ��Ԫ�أ�"+jedis.srandmember("dstkey"));
	}
	
	/**
	 * hash
	 */
	private void hashOperate(){
		System.out.println(jedis.flushDB());
		System.out.println("������Ϊkey��hash�����Ԫ��field1��"+shardedJedis.hset("key", "field1", "value1")); 
        System.out.println("������Ϊkey��hash�����Ԫ��field2��"+shardedJedis.hset("key", "field2", "value2")); 
        System.out.println("������Ϊkey��hash�����Ԫ��field3��"+shardedJedis.hset("key", "field3", "value3"));
        System.out.println("��������Ϊkey��hash��field1��Ӧ��value��"+shardedJedis.hget("key", "field1"));
        System.out.println("��������Ϊkey��hash��field1��Ӧ��value��"+shardedJedis.hmget("key", "field1","field2","field3"));
        System.out.println("ɾ������Ϊkey��hash�м�Ϊfield1����"+shardedJedis.hdel("key", "field1"));
        System.out.println("��������Ϊkey��hash�����м���Ӧ��value��"+shardedJedis.hvals("key"));
        System.out.println("��������Ϊkey��hash��Ԫ�ظ�����"+shardedJedis.hlen("key"));
        System.out.println("��������Ϊkey��hash�����м���"+shardedJedis.hkeys("key"));
        System.out.println("��������Ϊkey��hash�����еļ���field�������Ӧ��value��"+shardedJedis.hgetAll("key"));
        System.out.println("����Ϊkey��hash���Ƿ���ڼ�Ϊfield1����"+shardedJedis.hexists("key","field1"));
	}
}