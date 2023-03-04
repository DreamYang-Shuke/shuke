package com.polyPool.scheduled;

import com.polyPool.service.XbbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2022-07-07 15:27
 */
@Slf4j
@Component
public class Job {

	@Autowired
	private XbbService xbbService;
	
	/**
	 * 每天6点 执行
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void execute() {
		
	}
}
