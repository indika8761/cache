package lk.cache.strategies;

import java.util.Map;
import java.util.TreeMap;

public abstract class Strategy<K> {
	
	private final Map<K, Long> objectsStorage = new TreeMap<>();
	private final TreeMap<K, Long> sortedObjectsStorage = new TreeMap<>(new ComparatorImpl<K>(objectsStorage));
	
	public abstract void put(K key);
	
    public void remove(K key) {
        if (isObjectPresent(key)) {
            objectsStorage.remove(key);
        }
    }

    public boolean isObjectPresent(K key) {
        return objectsStorage.containsKey(key);
    }

    public K getReplacedKey() {
        sortedObjectsStorage.putAll(objectsStorage);
        return sortedObjectsStorage.firstKey();
    }

    public void clear() {
        objectsStorage.clear();
    }

	public Map<K, Long> getObjectsStorage() {
		return objectsStorage;
	}

	public TreeMap<K, Long> getSortedObjectsStorage() {
		return sortedObjectsStorage;
	}
    
    
	
	

}
