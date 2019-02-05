package com.myapp.senier.controller;

import javax.annotation.Resource;

import com.myapp.senier.model.DataModel;
import com.myapp.senier.service.LogAnalysisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class SchedulingController {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingController.class);

    @Resource(name = "LogAnalysisService")
    private LogAnalysisService logAnalysisService;

    @PostMapping(value="/jobs/{serviceCd}")
    public DataModel postMethodName(@RequestBody DataModel model, @PathVariable String serviceCd) {
        logger.info("params - {}", model);
        try {
            model.put("serviceCd", serviceCd);
            model = logAnalysisService.executeLogAnalyzer(model);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    
}