package lk.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lk.cache.strategies.StrategyType;

@Configuration()
@PropertySource("classpath:cacheProperties.properties")
public class CacheProperties {

	@Value("${cache.memoryCapacity}")
	private Integer memoryCapacity;

	@Value("${cache.fileCapacity}")
	private Integer fileCapacity;

	@Value("${cache.strategyType}")
	private StrategyType strategyType;

	public Integer getMemoryCapacity() {
		return memoryCapacity;
	}

	public void setMemoryCapacity(Integer memoryCapacity) {
		this.memoryCapacity = memoryCapacity;
	}

	public Integer getFileCapacity() {
		return fileCapacity;
	}

	public void setFileCapacity(Integer fileCapacity) {
		this.fileCapacity = fileCapacity;
	}

	public StrategyType getStrategyType() {
		return strategyType;
	}

	public void setStrategyType(StrategyType strategyType) {
		this.strategyType = strategyType;
	}

}
