package com.polyPool.controller;

import com.polyPool.service.ApiService;
import com.polyPool.service.XbbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提供给 聚水潭 的接口
 * @date 2022-06-22 16:07
 */
@Slf4j
@RestController
public class ApiController {

	@Autowired
	private ApiService apiService;
}
