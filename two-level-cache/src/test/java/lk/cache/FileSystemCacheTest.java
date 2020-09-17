package lk.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


public class FileSystemCacheTest {
    private static final String STRING_OBJECT1 = "value1";
    private static final String STRING_OBJECT2 = "value2";

    private FileSystemCache<Integer, String> fileSystemCache;

    @Before
    public void init() throws IOException {
        fileSystemCache = new FileSystemCache<>();
    }

    @After
    public void clearCache() {
        fileSystemCache.clear();
    }

    @Test
    public void cachePutGetAndRemoveObjectTest() {
        fileSystemCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, fileSystemCache.get(0));
        assertEquals(1, fileSystemCache.size());

        fileSystemCache.remove(0);
        assertNull(fileSystemCache.get(0));
    }

    @Test
    public void objectNotExistsTest() {
        fileSystemCache.put(0, STRING_OBJECT1);
        assertEquals(STRING_OBJECT1, fileSystemCache.get(0));
        assertNull(fileSystemCache.get(7));
    }


    @Test
    public void cacheSizeTest() {
        fileSystemCache.put(0, STRING_OBJECT1);
        assertEquals(1, fileSystemCache.size());

        fileSystemCache.put(1, STRING_OBJECT2);
        assertEquals(2, fileSystemCache.size());
    }

    @Test
    public void isObjectPresentTest() {
        assertFalse(fileSystemCache.isObjectPresent(0));

        fileSystemCache.put(0, STRING_OBJECT1);
        assertTrue(fileSystemCache.isObjectPresent(0));
    }

    @Test
    public void isEmptyPlaceTest() throws IOException {
        fileSystemCache = new FileSystemCache<>(5);

        IntStream.range(0, 4).forEach(i -> fileSystemCache.put(i, "String " + i));
        assertTrue(fileSystemCache.hasEmptyPlace());
        fileSystemCache.put(5, "String");
        assertFalse(fileSystemCache.hasEmptyPlace());
    }

    @Test
    public void ClearCacheTest() {
        IntStream.range(0, 3).forEach(i -> fileSystemCache.put(i, "String " + i));

        assertEquals(3, fileSystemCache.size());
        fileSystemCache.clear();
        assertEquals(0, fileSystemCache.size());
    }
}
