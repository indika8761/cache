package lk.cache;

public interface Cache<K, V> {

	void put(K key, V value);

	V get(K key);

	void remove(K key);

	int size();

	boolean isObjectPresent(K key);

	boolean hasEmptyPlace();

	void clear();

}
