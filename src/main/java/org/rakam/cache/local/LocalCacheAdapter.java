package org.rakam.cache.local;

import org.rakam.cache.SimpleCacheAdapter;
import org.vertx.java.core.json.JsonObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by buremba on 21/05/14.
 */
public class LocalCacheAdapter implements SimpleCacheAdapter {
    static HashMap<String, AtomicLong> counters = new HashMap();
    static HashMap<String, Set<String>> sets = new HashMap();

    @Override
    public Long getCounter(String key) {
        AtomicLong a = counters.get(key);
        return (a==null) ? 0 : a.get();
    }


    @Override
    public int getSetCount(String key) {
        Set<String> a = sets.get(key);
        return (a==null) ? 0 : a.size();
    }

    @Override
    public Iterator<String> getSetIterator(String key) {
        return sets.get(key).iterator();
    }

    @Override
    public Long incrementCounter(String key) {
        AtomicLong a = counters.get(key);
        if(a==null) {
            counters.put(key, new AtomicLong(1));
            return 1L;
        }else {
            return a.incrementAndGet();
        }
    }

    @Override
    public void addGroupByItem(String aggregation, String groupBy, String item) {
        AtomicLong counter = counters.get(aggregation + ":" + item);
        if (counter.getAndIncrement()==0) {
            sets.get(aggregation + "::" + "keys").add(item);
        }
    }

    @Override
    public void addGroupByItem(String aggregation, String groupBy, String item, Long incrementBy) {
        AtomicLong counter = counters.get(aggregation + ":" + item);
        if (counter.getAndAdd(incrementBy)==0) {
            sets.get(aggregation + "::" + "keys").add(item);
        }
    }

    @Override
    public JsonObject getActorProperties(String project, String actor_id) {
        return null;
    }

    @Override
    public void addActorProperties(String project, String actor_id, JsonObject properties) {
    }

    @Override
    public Long incrementCounter(String key, long increment) {
        return counters.get(key).incrementAndGet();
    }

    @Override
    public void setCounter(String s, long target) {
        counters.get(s).set(target);
    }

    @Override
    public void addToSet(String setName, String item) {
        sets.get(setName).add(item);
    }

    @Override
    public void addToSet(String setName, Collection<String> items) {
        sets.get(setName).addAll(items);
    }

    @Override
    public void setActorProperties(String project, String actor_id, JsonObject properties) {

    }

    public void flush() {
        sets.clear();
        counters.clear();
    }
}
