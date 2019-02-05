package com.myapp.senier.service;

import com.myapp.senier.model.DataModel;

public interface LogAnalysisService {
    // 키워드와 로그 데이터 비교. 형태소 db 저장, 로그 히스토리 저장. 로그 분석 결과 리턴.
    public DataModel executeLogAnalyzer(DataModel logMap) throws Exception;
        
}