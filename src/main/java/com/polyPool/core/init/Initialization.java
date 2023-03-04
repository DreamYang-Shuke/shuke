package com.polyPool.core.init;

import com.polyPool.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2022-06-23 14:35
 */
@Component
@Order(value = 1)
public class Initialization implements CommandLineRunner {

	@Autowired
	private ApiService apiService;

	@Override
	public void run(String... args) throws Exception {
		apiService.startFlushToken();
	}
}
