package lk.cache.strategies;

public class LRU<K> extends Strategy<K> {

	@Override
	public void put(K key) {
		  getObjectsStorage().put(key, System.nanoTime());	
	}

}
