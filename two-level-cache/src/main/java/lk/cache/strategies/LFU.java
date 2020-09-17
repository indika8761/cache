package lk.cache.strategies;

public class LFU<K> extends Strategy<K> {

	@Override
	public void put(K key) {
		long frequency = 1;
		if (getObjectsStorage().containsKey(key)) {
			frequency = getObjectsStorage().get(key) + 1;
		}
		getObjectsStorage().put(key, frequency);
	}

}
