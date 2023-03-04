package com.polyPool.core.init;

import com.polyPool.service.XbbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2022-06-23 14:35
 */
@Component
@Order(value = 3)
public class InitContract implements CommandLineRunner {

	@Autowired
    private XbbService xbbService;
    
    @Override
    public void run(String... args) throws Exception {
        xbbService.startContract();
    }
}
