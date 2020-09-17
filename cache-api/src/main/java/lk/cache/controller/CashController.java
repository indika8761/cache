package lk.cache.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lk.cache.CacheHandler;

@RestController
@RequestMapping("/api/cache")
public class CashController {
	
	private final Logger logger = LoggerFactory.getLogger(CashController.class);
	
	@Autowired
	CacheHandler cacheHandler;

	@PostMapping(value = "/{key}")
	public void put(@RequestBody Object cacheObject, @PathVariable Integer key) {

		try {
			cacheHandler.getInstance().put(key, new CacheObject(cacheObject));
		} catch (IOException e) {
			logger.error("error : " + e.getMessage());
			
		}
	}

	@GetMapping(value = "/{key}")
	public Object get(@PathVariable Integer key) {

		try {
			CacheObject cacheObject = cacheHandler.getInstance().get(key);

			if (cacheObject != null) {
				return cacheObject.getCacheData();
			}
		} catch (IOException e) {
			logger.error("error : " + e.getMessage());
		}

		return null;
	}

}
