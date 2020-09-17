package lk.cache;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lk.cache.strategies.LFU;
import lk.cache.strategies.LRU;
import lk.cache.strategies.Strategy;
import lk.cache.strategies.StrategyType;

public class TwoLevelCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

	private final MemoryCache<K, V> firstLevelCache;
	private final FileSystemCache<K, V> secondLevelCache;
	private final Strategy<K> strategy;
	
	private final Logger logger = LoggerFactory.getLogger(TwoLevelCache.class);

	public TwoLevelCache(final int memoryCapacity, final int fileCapacity, final StrategyType strategyType)
			throws IOException {
		this.firstLevelCache = new MemoryCache<>(memoryCapacity);
		this.secondLevelCache = new FileSystemCache<>(fileCapacity);
		this.strategy = getStrategy(strategyType);
	}

	public TwoLevelCache(final int memoryCapacity, final int fileCapacity) throws IOException {
		this.firstLevelCache = new MemoryCache<>(memoryCapacity);
		this.secondLevelCache = new FileSystemCache<>(fileCapacity);
		this.strategy = getStrategy(StrategyType.LFU);
	}

	private Strategy<K> getStrategy(StrategyType strategyType) {
		switch (strategyType) {
		case LRU:
			return new LRU<>();
		case LFU:
		default:
			return new LFU<>();
		}
	}

	@Override
	public void put(K key, V value) {
		if (firstLevelCache.isObjectPresent(key) || firstLevelCache.hasEmptyPlace()) {
			logger.debug(String.format("Put object with key %s to the 1st level", key));
			firstLevelCache.put(key, value);
			if (secondLevelCache.isObjectPresent(key)) {
				secondLevelCache.remove(key);
			}
		} else if (secondLevelCache.isObjectPresent(key) || secondLevelCache.hasEmptyPlace()) {
			logger.debug(String.format("Put object with key %s to the 2nd level", key));
			secondLevelCache.put(key, value);
		} else {
			replaceObject(key, value);
		}

		if (!strategy.isObjectPresent(key)) {
			logger.debug(String.format("Put object with key %s to strategy", key));
			strategy.put(key);
		}

	}

	private void replaceObject(K key, V value) {
		K replacedKey = strategy.getReplacedKey();
		if (firstLevelCache.isObjectPresent(replacedKey)) {
			logger.debug(String.format("Replace object with key %s from 1st level", replacedKey));
			firstLevelCache.remove(replacedKey);
			firstLevelCache.put(key, value);
		} else if (secondLevelCache.isObjectPresent(replacedKey)) {
			logger.debug(String.format("Replace object with key %s from 2nd level", replacedKey));
			secondLevelCache.remove(replacedKey);
			secondLevelCache.put(key, value);
		}
	}

	@Override
	public V get(K key) {
		if (firstLevelCache.isObjectPresent(key)) {
			strategy.put(key);
			return firstLevelCache.get(key);
		} else if (secondLevelCache.isObjectPresent(key)) {
			strategy.put(key);
			return secondLevelCache.get(key);
		}
		return null;
	}

	@Override
	public void remove(K key) {
		if (firstLevelCache.isObjectPresent(key)) {
			logger.debug(String.format("Remove object with key %s from 1st level", key));
			firstLevelCache.remove(key);
		}
		if (secondLevelCache.isObjectPresent(key)) {
			logger.debug(String.format("Remove object with key %s from 2nd level", key));
			secondLevelCache.remove(key);
		}
		strategy.remove(key);

	}

	@Override
	public int size() {
		return firstLevelCache.size() + secondLevelCache.size();
	}

	@Override
	public boolean isObjectPresent(K key) {
		return firstLevelCache.isObjectPresent(key) || secondLevelCache.isObjectPresent(key);
	}

	@Override
	public boolean hasEmptyPlace() {
		return firstLevelCache.hasEmptyPlace() || secondLevelCache.hasEmptyPlace();
	}

	@Override
	public void clear() {
		firstLevelCache.clear();
		secondLevelCache.clear();
		strategy.clear();

	}

	public MemoryCache<K, V> getFirstLevelCache() {
		return firstLevelCache;
	}

	public FileSystemCache<K, V> getSecondLevelCache() {
		return secondLevelCache;
	}

	public Strategy<K> getStrategy() {
		return strategy;
	}
	
	

}
