package lk.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


public class MemoryCacheTest {
    private static final String STRING_OBJECT1 = "value1";
    private static final String STRING_OBJECT2 = "value2";

    private MemoryCache<Integer, String> memoryCache;

    @Before
    public void init() throws IOException {
    	memoryCache = new MemoryCache<>(3);
    }

    @After
    public void clearCache() {
    	memoryCache.clear();
    }

    @Test
    public void cachePutGetAndRemoveObjectTest() {
    	memoryCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, memoryCache.get(0));
        assertEquals(1, memoryCache.size());

        memoryCache.remove(0);
        assertNull(memoryCache.get(0));
    }

    @Test
    public void objectNotExistsTest() {
    	memoryCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, memoryCache.get(0));
        assertNull(memoryCache.get(8));
    }


    @Test
    public void cacheSizeTest() {
    	memoryCache.put(0, STRING_OBJECT1);
        assertEquals(1, memoryCache.size());

        memoryCache.put(1, STRING_OBJECT2);
        assertEquals(2, memoryCache.size());
    }

    @Test
    public void isObjectPresentTest() {
        assertFalse(memoryCache.isObjectPresent(0));

        memoryCache.put(0, STRING_OBJECT1);
        assertTrue(memoryCache.isObjectPresent(0));
    }

    @Test
    public void isEmptyPlaceTest() throws IOException {
    	memoryCache = new MemoryCache<>(5);

        IntStream.range(0, 4).forEach(i -> memoryCache.put(i, "String " + i));
        assertTrue(memoryCache.hasEmptyPlace());
        memoryCache.put(5, "String");
        assertFalse(memoryCache.hasEmptyPlace());
    }

    @Test
    public void ClearCacheTest() {
        IntStream.range(0, 3).forEach(i -> memoryCache.put(i, "String " + i));

        assertEquals(3, memoryCache.size());
        memoryCache.clear();
        assertEquals(0, memoryCache.size());
    }
}
