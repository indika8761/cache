package lk.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

	private final Map<K, V> objectsStorage;
	private final int capacity;
	

	MemoryCache(int capacity) {
		this.capacity = capacity;
		this.objectsStorage = new ConcurrentHashMap<>(capacity);
	}

	@Override
	public void put(K key, V value) {
		objectsStorage.put(key, value);

	}

	@Override
	public V get(K key) {
		return objectsStorage.get(key);
	}

	@Override
	public void remove(K key) {
		objectsStorage.remove(key);

	}

	@Override
	public int size() {
		return objectsStorage.size();
	}

	@Override
	public boolean isObjectPresent(K key) {
		return objectsStorage.containsKey(key);
	}

	@Override
	public boolean hasEmptyPlace() {
		return size() < this.capacity;
	}

	@Override
	public void clear() {
		objectsStorage.clear();
	}

}
