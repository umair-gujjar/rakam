package org.rakam.cache.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;
import org.rakam.cache.SimpleCacheAdapter;
import org.vertx.java.core.json.JsonObject;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by buremba on 21/12/13.
 */

public class HazelcastCacheAdapter implements SimpleCacheAdapter {
    private static HazelcastInstance hazelcast;

    public HazelcastCacheAdapter() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("analytics").setPassword("");
        hazelcast =  HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Override
    public void addGroupByItem(String aggregation, String groupBy, String item) {
        IAtomicLong counter = hazelcast.getAtomicLong(aggregation + ":" + item);
        if (counter.getAndIncrement()==0) {
            hazelcast.getSet(aggregation + "::" + "keys").add(item);
        }
    }

    @Override
    public void addGroupByItem(String aggregation, String groupBy, String item, Long incrementBy) {
        IAtomicLong counter = hazelcast.getAtomicLong(aggregation + ":" + item);
        if (counter.getAndAdd(incrementBy)==0) {
            hazelcast.getSet(aggregation + "::" + "keys").add(item);
        }
    }

    @Override
    public JsonObject getActorProperties(String project, String actor_id) {
        IMap<String, JsonObject> map = hazelcast.getMap(project + ":actor-prop");
        return map.get(actor_id);
    }

    @Override
    public void addActorProperties(String project, String actor_id, JsonObject properties) {
        IMap<String, JsonObject> map = hazelcast.getMap(project+":actor-prop");
        map.put(actor_id, properties);
    }

    @Override
    public Long incrementCounter(String key, long increment) {
        return hazelcast.getAtomicLong(key).getAndAdd(increment);
    }

    @Override
    public void setCounter(String key, long target) {
        hazelcast.getAtomicLong(key).set(target);
    }

    @Override
    public void addToSet(String setName, String item) {
        hazelcast.getSet(setName).add(item);
    }

    @Override
    public void addToSet(String setName, Collection<String> items) {
        hazelcast.getSet(setName).addAll(items);
    }

    @Override
    public void setActorProperties(String project, String actor_id, JsonObject properties) {
        hazelcast.getMap(project+":actor-prop").put(actor_id, properties);
    }

    @Override
    public void flush() {

    }

    @Override
    public Long getCounter(String key) {
        return hazelcast.getAtomicLong(key).get();

    }

    @Override
    public int getSetCount(String key) {
       return hazelcast.getSet(key).size();
    }

    @Override
    public Iterator getSetIterator(String key) {
        return hazelcast.getSet(key).iterator();
    }

    @Override
    public Long incrementCounter(String key) {
        return hazelcast.getAtomicLong(key.toString()).incrementAndGet();
    }



}
