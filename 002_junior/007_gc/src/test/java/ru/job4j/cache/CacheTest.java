package ru.job4j.cache;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CacheTest {


    @Test
    @SuppressWarnings({"NewObjectEquality", "SimplifiableJUnitAssertion"})
    public void whenTakeSameKeyThenCachedObjectReturned() throws Exception {
        var cache = new Cache<>((IReadObjectValue<String, Object>) key -> new Object());
        var newObject = new Object();
        var aKeyOne = cache.get("a");
        var aKeyTwo = cache.get("a");
        var bKey = cache.get("b");
        assertTrue(aKeyOne == aKeyTwo);
        assertTrue(newObject != aKeyOne);
        assertTrue(aKeyOne != bKey);
    }
}