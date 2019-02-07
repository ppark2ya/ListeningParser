package com.myapp.senier.service;

import java.util.List;

import com.myapp.senier.model.DataModel;

public interface LogAnalysisService {
    public DataModel getServerInfo(String serviceCd);
    // 키워드와 로그 데이터 비교. 형태소 db 저장, 로그 히스토리 저장. 로그 분석 결과 리턴.
    public DataModel executeLogAnalyzer(DataModel logMap) throws Exception;
    public DataModel sendMessageToManagers(DataModel model) throws Exception;
}