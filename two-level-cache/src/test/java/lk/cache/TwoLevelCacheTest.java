package lk.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lk.cache.strategies.StrategyType;



public class TwoLevelCacheTest {
    private static final String STRING_OBJECT1 = "value1";
    private static final String STRING_OBJECT2 = "value2";
    private static final String STRING_OBJECT3 = "value3";

    private TwoLevelCache<Integer, String> twoLevelCache;

    @Before
    public void init() throws IOException {
        twoLevelCache = new TwoLevelCache<>(1, 1);
    }

    @After
    public void clearCache() {
        twoLevelCache.clear();
    }

    @Test
    public void cachePutGetAndRemoveObjectTest() {
        twoLevelCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, twoLevelCache.get(0));
        assertEquals(1, twoLevelCache.size());

        twoLevelCache.remove(0);
        assertNull(twoLevelCache.get(0));
    }

    @Test
    public void removeObjectFromFirstLevelTest() {
        twoLevelCache.put(0, STRING_OBJECT1);
        twoLevelCache.put(1, STRING_OBJECT2);

        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertEquals(STRING_OBJECT2, twoLevelCache.getSecondLevelCache().get(1));

        twoLevelCache.remove(0);

        assertNull(twoLevelCache.getFirstLevelCache().get(0));
        assertEquals(STRING_OBJECT2, twoLevelCache.getSecondLevelCache().get(1));
    }

    @Test 
    public void removeObjectFromSecondLevelTest() {
        twoLevelCache.put(0, STRING_OBJECT1);
        twoLevelCache.put(1, STRING_OBJECT1);

        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertEquals(STRING_OBJECT1, twoLevelCache.getSecondLevelCache().get(1));

        twoLevelCache.remove(1);

        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertNull(twoLevelCache.getSecondLevelCache().get(1));
    }



    @Test
    public void removeDuplicatedObjectFromSecondLevelWhenFirstLevelHasEmptyPlaceTest() {
        assertTrue(twoLevelCache.getFirstLevelCache().hasEmptyPlace());

        twoLevelCache.getSecondLevelCache().put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, twoLevelCache.getSecondLevelCache().get(0));

        twoLevelCache.put(0, STRING_OBJECT1);

        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertFalse(twoLevelCache.getSecondLevelCache().isObjectPresent(0));
    }

    @Test
    public void putObjectIntoCacheWhenFirstLevelHasEmptyPlaceTest() {
        assertTrue(twoLevelCache.getFirstLevelCache().hasEmptyPlace());
        twoLevelCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, twoLevelCache.get(0));
        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertFalse(twoLevelCache.getSecondLevelCache().isObjectPresent(0));
    }

    @Test
    public void putObjectIntoCacheWhenObjectExistsInFirstLevelCacheTest() {
        twoLevelCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, twoLevelCache.get(0));
        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertEquals(1, twoLevelCache.getFirstLevelCache().size());

       
        twoLevelCache.put(0, STRING_OBJECT2);

        assertEquals(STRING_OBJECT2, twoLevelCache.get(0));
        assertEquals(STRING_OBJECT2, twoLevelCache.getFirstLevelCache().get(0));
        assertEquals(1, twoLevelCache.getFirstLevelCache().size());
    }

    @Test
    public void putObjectIntoCacheWhenSecondLevelHasEmptyPlaceTest() {
        IntStream.range(0, 1).forEach(i -> twoLevelCache.put(i, "String " + i));

        assertFalse(twoLevelCache.getFirstLevelCache().hasEmptyPlace());
        assertTrue(twoLevelCache.getSecondLevelCache().hasEmptyPlace());

        twoLevelCache.put(2, STRING_OBJECT2);

        assertEquals(STRING_OBJECT2, twoLevelCache.get(2));
        assertEquals(STRING_OBJECT2, twoLevelCache.getSecondLevelCache().get(2));
    }

    @Test
    public void putObjectIntoCacheWhenObjectExistsInSecondLevelTest() {
        IntStream.range(0, 1).forEach(i -> twoLevelCache.put(i, "String " + i));

        assertFalse(twoLevelCache.getFirstLevelCache().hasEmptyPlace());

        twoLevelCache.put(2, STRING_OBJECT2);

        assertEquals(STRING_OBJECT2, twoLevelCache.get(2));
        assertEquals(STRING_OBJECT2, twoLevelCache.getSecondLevelCache().get(2));
        assertEquals(1, twoLevelCache.getSecondLevelCache().size());

        
        twoLevelCache.put(2, STRING_OBJECT3);

        assertEquals(STRING_OBJECT3, twoLevelCache.get(2));
        assertEquals(STRING_OBJECT3, twoLevelCache.getSecondLevelCache().get(2));
        assertEquals(1, twoLevelCache.getSecondLevelCache().size());
    }

    @Test
    public void putObjectIntoCacheWhenObjectShouldBeReplacedTest() {
        IntStream.range(0, 2).forEach(i -> twoLevelCache.put(i, "String " + i));

        assertFalse(twoLevelCache.hasEmptyPlace());
        assertFalse(twoLevelCache.getStrategy().isObjectPresent(3));

        twoLevelCache.put(3, STRING_OBJECT3);

        assertEquals(twoLevelCache.get(3), STRING_OBJECT3);
        assertTrue(twoLevelCache.getStrategy().isObjectPresent(3));
        assertTrue(twoLevelCache.getFirstLevelCache().isObjectPresent(3));
        assertFalse(twoLevelCache.getSecondLevelCache().isObjectPresent(3));
    }

    @Test
    public void cacheSizeTest() {
        twoLevelCache.put(0, STRING_OBJECT1);
        assertEquals(1, twoLevelCache.size());

        twoLevelCache.put(1, STRING_OBJECT2);
        assertEquals(2, twoLevelCache.size());
    }

    @Test
    public void isObjectPresentTest() {
        assertFalse(twoLevelCache.isObjectPresent(0));

        twoLevelCache.put(0, STRING_OBJECT1);
        assertTrue(twoLevelCache.isObjectPresent(0));
    }

    @Test
    public void isEmptyPlaceTest() {
        assertFalse(twoLevelCache.isObjectPresent(0));
        twoLevelCache.put(0, STRING_OBJECT1);
        assertTrue(twoLevelCache.hasEmptyPlace());

        twoLevelCache.put(1, STRING_OBJECT2);
        assertFalse(twoLevelCache.hasEmptyPlace());
    }

    @Test
    public void shouldClearCacheTest() {
        twoLevelCache.put(0, STRING_OBJECT1);
        twoLevelCache.put(1, STRING_OBJECT2);

        assertEquals(2, twoLevelCache.size());
        assertTrue(twoLevelCache.getStrategy().isObjectPresent(0));
        assertTrue(twoLevelCache.getStrategy().isObjectPresent(1));

        twoLevelCache.clear();

        assertEquals(0, twoLevelCache.size());
        assertFalse(twoLevelCache.getStrategy().isObjectPresent(0));
        assertFalse(twoLevelCache.getStrategy().isObjectPresent(1));
    }

    @Test
    public void shouldUseLRUStrategyTest() throws IOException {
        twoLevelCache = new TwoLevelCache<>(1, 1, StrategyType.LRU);
        twoLevelCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, twoLevelCache.get(0));
        assertEquals(STRING_OBJECT1, twoLevelCache.getFirstLevelCache().get(0));
        assertFalse(twoLevelCache.getSecondLevelCache().isObjectPresent(0));
    }


}