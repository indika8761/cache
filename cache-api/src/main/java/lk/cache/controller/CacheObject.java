package lk.cache.controller;

import java.io.Serializable;

public class CacheObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1479809042595019784L;

	private final Object cacheData;

	public CacheObject(Object cacheData) {
		super();
		this.cacheData = cacheData;
	}

	public Object getCacheData() {
		return cacheData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cacheData == null) ? 0 : cacheData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheObject other = (CacheObject) obj;
		if (cacheData == null) {
			if (other.cacheData != null)
				return false;
		} else if (!cacheData.equals(other.cacheData))
			return false;
		return true;
	}
	
}
