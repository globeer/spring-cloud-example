package com.gnu.eureka.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@Service
public class ClientService {
	
	private RestTemplate restTemplate;
	private final String CLIENT_SERVICE_2 = "client-service-2";
	private final FeignClientService feignClient;
	public ClientService(RestTemplate restTemplate, FeignClientService feignClient) {
		this.restTemplate = restTemplate;
		this.feignClient = feignClient;
	}
	/**
	 * 
	 * request with hystrix proxy (fallback and circuit breaker)
	 * 
	 * <pre>
	 * to use @HystrixCommand annnotation, classpath requires 'spring-cloud-starter-netflix-hystrix' dependency
	 * though netflix-eureka-client and ribbon include netflix-hystrix and hystrix-core, this annotation belongs to hystrix-javanica in 'spring-cloud-starter-netflix-hystrix'  
	 * </pre>
	 * @see {@link <a href="https://github.com/Netflix/Hystrix/wiki/Configuration">Configuration</a>} 
	 * @return
	 */
	@HystrixCommand(
			fallbackMethod="userListFallback",
			commandProperties = {
					// if at least, five request sent and more 20% fails, stop for 10000ms (circuit open, it means that automatically triggers fallback for 10seconds)
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="5"),
					@HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="20"),
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="10000"),
					/*
					 * THREAD - executes on separate thread, limited by thread pool
					 * SEMAPHORE - executes on calling thread, limited by calling semaphore count 
					 */
					@HystrixProperty(name="execution.isolation.strategy", value="THREAD")
			}
			)
	public String userList() {
		return restTemplate.getForObject("http://" + CLIENT_SERVICE_2 + "/userList", String.class);
	}
	
	public String userListByFeign() {
		return feignClient.userList();
	}
	
	/**
	 * 
	 * fallback method for userList()
	 * if client-service-2 causes exception, this method returns error to prevent propagation.
	 * 
	 * @return
	 */
	public String userListFallback() {
		return "error";
	}
}
