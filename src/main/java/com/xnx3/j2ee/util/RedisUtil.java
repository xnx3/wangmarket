package com.xnx3.j2ee.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.springframework.util.StringUtils;
import com.xnx3.Lang;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
    //Redis服务器IP
	public static String host = "127.0.0.1";

    //Redis的端口号
    public static int port = 6379;

    //访问密码
//    private static String AUTH = "pwd";
    public static String password = null;

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    public static int timeout = 3000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;
    
    /**
     * 初始化Redis连接池
     */
    static {
    	host = ApplicationPropertiesUtil.getProperty("spring.redis.host");
    	String portStr = ApplicationPropertiesUtil.getProperty("spring.redis.port");
    	if(portStr != null){
    		port = Lang.stringToInt(portStr, -1);
    		if(port == -1){
    			ConsoleUtil.info("redis未设置port，则使用默认的6379");
    			port = 6379;
    		}
    	}
    	password = ApplicationPropertiesUtil.getProperty("spring.redis.password");
    	timeout = Lang.stringToInt(ApplicationPropertiesUtil.getProperty("spring.redis.timeout"), 3000);
    	
    	
    	if(host != null){
    		try {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(MAX_ACTIVE);
                config.setMaxIdle(MAX_IDLE);
                config.setMaxWaitMillis(MAX_WAIT);
                config.setTestOnBorrow(TEST_ON_BORROW);
                jedisPool = new JedisPool(config, host, port, timeout, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}else{
    		//不使用redis
    	}
    }

    /**
     * 当前是否使用redis
     * @return true:使用redis  false:不使用redis
     */
    public static boolean isUse(){
    	return jedisPool != null;
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取redis键值-object
     * 
     * @param key
     * @return
     */
    public static Object getObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if(!StringUtils.isEmpty(bytes)) {
                return SerializeUtils.deserialize(bytes);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 设置redis键值-object
     * @param key
     * @param value
     * @param expiretime
     * @return
     */
    public static String setObject(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key.getBytes(), SerializeUtils.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(jedis != null)
            {
                jedis.close();
            }
        }
    }
    
    /**
     * 设定key-value
     * @param expiretime 当前key-value的过期时间，单位是秒。比如设定为2，则超过2秒后没使用，会自动删除调
     * @return
     */
    public static String setObject(String key, Object value,int expiretime) {
        String result = "";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key.getBytes(), SerializeUtils.serialize(value));
            if(result.equals("OK")) {
                jedis.expire(key.getBytes(), expiretime);
            }
            return result;
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if(jedis != null)
            {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 删除key
     */
    public static Long delkeyObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            if(jedis != null)
            {
                jedis.close();
            }
        }
    }

    public static Boolean existsObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            if(jedis != null)
            {
                jedis.close();
            }
        }
    }
    
    
    /**
	 * 获取
	 */
	public static Object deserialize(byte[] bytes) {
		Object result = null;
		if (isEmpty(bytes)) {
			return null;
		}
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
				try {
					result = objectInputStream.readObject();
				}
				catch (ClassNotFoundException ex) {
					throw new Exception("Failed to deserialize object type", ex);
				}
			}
			catch (Throwable ex) {
				throw new Exception("Failed to deserialize", ex);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			ConsoleUtil.debug(e.getMessage());
		}
		return result;
	}
	public static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}
	public static byte[] serialize(Object object) {
		byte[] result = null;
		if (object == null) {
			return new byte[0];
		}
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
			try  {
				if (!(object instanceof Serializable)) {
					throw new IllegalArgumentException(SerializeUtils.class.getSimpleName() + " requires a Serializable payload " +
							"but received an object of type [" + object.getClass().getName() + "]");
				}
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
				objectOutputStream.writeObject(object);
				objectOutputStream.flush();
				result =  byteStream.toByteArray();
			}
			catch (Throwable ex) {
				throw new Exception("Failed to serialize", ex);
			}
		} catch (Exception ex) {
//			ex.printStackTrace();
			ConsoleUtil.debug(ex.getMessage());
		}
		return result;
	}
    
    public static void main(String[] args) {
    	//setObject("1", "11", 5);
    	
    	System.out.println(delkeyObject("plugin_map_userid_*"));
    	
	}
}
/*
 * 序列化
 */
class SerializeUtils{
	/**
	 * 反序列化
	 */
	public static Object deserialize(byte[] bytes) {
		Object result = null;
		if (isEmpty(bytes)) {
			return null;
		}
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
				try {
					result = objectInputStream.readObject();
				}
				catch (ClassNotFoundException ex) {
					throw new Exception("Failed to deserialize object type", ex);
				}
			}
			catch (Throwable ex) {
				throw new Exception("Failed to deserialize", ex);
			}
		} catch (Exception e) {
			ConsoleUtil.debug(e.getMessage());
		}
		return result;
	}
	public static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}
	/**
	 * 序列化
	 */
	public static byte[] serialize(Object object) {
		byte[] result = null;
		if (object == null) {
			return new byte[0];
		}
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
			try  {
				if (!(object instanceof Serializable)) {
					throw new IllegalArgumentException(SerializeUtils.class.getSimpleName() + " requires a Serializable payload " +
							"but received an object of type [" + object.getClass().getName() + "]");
				}
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
				objectOutputStream.writeObject(object);
				objectOutputStream.flush();
				result =  byteStream.toByteArray();
			}
			catch (Throwable ex) {
				throw new Exception("Failed to serialize", ex);
			}
		} catch (Exception ex) {
			ConsoleUtil.debug(ex.getMessage());
		}
		return result;
	}
}