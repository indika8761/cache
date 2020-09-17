package lk.cache.strategies;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public class ComparatorImpl <K> implements Comparator<K>, Serializable {
    private static final long serialVersionUID = 1;

    private final Map<K, Long> comparatorMap;
    
    

    public ComparatorImpl(Map<K, Long> comparatorMap) {
		super();
		this.comparatorMap = comparatorMap;
	}



	@Override
    public int compare(K key1, K key2) {
		Long key1Long = comparatorMap.get(key1);
		Long key2Long = comparatorMap.get(key2);

        return key1Long.compareTo(key2Long);
    }
}



