package com.polyPool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 * Company:
 *
 * @author chenshan
 * @version v1.0
 * @since 2022/5/9 10:17
 */
@SpringBootApplication
@RestController
public class PolyPoolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolyPoolApiApplication.class, args);
    }
}
