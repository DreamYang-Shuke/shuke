package com.polyPool.scheduled;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @date 2022-07-07 15:35
 */
@Configuration
@EnableScheduling //启用定时任务
//配置文件读取是否启用此配置
@ConditionalOnProperty(prefix = "enable", name = "scheduled", havingValue = "true")
public class SchedulingConfig {
}
