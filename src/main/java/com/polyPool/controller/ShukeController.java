package com.polyPool.controller;

import com.alibaba.fastjson.JSON;
import com.polyPool.model.XbbBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: wujian
 * @create: 2023-03-04 16:52
 * @description: 小程序Controller层
 **/
@Slf4j
@RestController
@RequestMapping(value = "/list")
public class ShukeController {

    @RequestMapping(value = "/get/price", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  String getParams(XbbBack request) throws IOException {
        int i = 1;
        for (int j = 0; j < 10; j++) {
            i++ ;
            System.out.println(i);
        }

        return null;
    }
}
