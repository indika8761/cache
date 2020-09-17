package lk.cache;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.cache.controller.CacheObject;

@Component
@Scope("singleton")
public class CacheHandler {

	@Autowired
	CacheProperties cacheProperties;
	
	public static TwoLevelCache<Integer, CacheObject> instance;
	
	
	public  TwoLevelCache<Integer, CacheObject> getInstance() throws IOException {

		if(instance==null) {
			synchronized (CacheHandler.class) {
				
				if(instance==null) {
					instance =new TwoLevelCache<>(cacheProperties.getMemoryCapacity(), cacheProperties.getFileCapacity(),
							cacheProperties.getStrategyType());
				}
			}
		}

		return instance;
	}

}
