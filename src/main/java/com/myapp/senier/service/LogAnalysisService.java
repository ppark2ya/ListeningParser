package com.myapp.senier.service;

import com.myapp.senier.model.DataModel;

public interface LogAnalysisService {
    public DataModel getServerInfo(String serviceCd);
    // 키워드와 로그 데이터 비교, 로그 히스토리 저장. 로그 분석 결과 리턴.
    public DataModel executeLogAnalyzer(DataModel logMap) throws Exception;
    // Sefilcare 로그 메세지 재조립
    public void reassemblyLogMessage(DataModel logMap) throws Exception;
    // 사용자에게 메세지 전송
    public DataModel sendMessageToManagers(DataModel model) throws Exception;
}