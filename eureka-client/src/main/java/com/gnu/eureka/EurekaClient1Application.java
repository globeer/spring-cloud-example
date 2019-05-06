package com.gnu.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**

 * Eureka Client Service
 * <pre>
 * On Spring boot 2.1.4, if project has a discovery-client dependency, 
 * it automatically tries to register discovery server itself. 
 * (no need to @EnableEurekaClient or @EnableDiscoveryClient)
 * </pre>
 * @author gnu-gnu(geunwoo.j.shim@gmail.com)
 * @see https://cloud.spring.io/spring-cloud-netflix/multi/multi__service_discovery_eureka_clients.html
 *
 */
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
public class EurekaClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClient1Application.class, args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
