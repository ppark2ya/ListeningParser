package com.myapp.senier.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.model.DataModel;
import com.myapp.senier.persistence.JobMapper;
import com.myapp.senier.service.LogAnalysisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DataModel logParser(@RequestBody DataModel model, @PathVariable String serviceCd) {
        DataModel resultMap = new DataModel();
        try {
            model.put("serviceCd", serviceCd);
            // 서버 정보 get
            DataModel serverInfo = logAnalysisService.getServerInfo(serviceCd);
            // 서버 사용 유무 체크
            if(serverInfo.getStrNull("useCl").equals(CommonConstant.USED)) {
                resultMap.putAll(logAnalysisService.executeLogAnalyzer(model));
            } else {
                logger.info("사용하지 않는 서버입니다.");
                resultMap.putStrNull("logStatus", CommonConstant.NOT_USED);
                return resultMap;
            }

            // 크리티컬한 상태일 경우 문자 메세지 발송
            // 유저 테이블의 권한을 코드테이블을 통해 조회 후 해당되는 유저에게 메세지 전송
            if(resultMap.getStrNull("logStatus").equals(CommonConstant.CRITICAL)) {
                //resultMap.putAll(logAnalysisService.sendMessageToManagers(resultMap));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    
}