package com.myapp.senier.persistence;

import java.util.List;

import com.myapp.senier.model.DataModel;

public interface JobMapper {
    // 키워드 리스트 조회
    public List<String> getOnlyKeywords(String serviceCd);
    // 키워드 테이블 조회
    public List<DataModel> getKeywordList(String serviceCd);
    // 가중치 계산을 통해 크리티컬 여부 조회
    public String isCritical(DataModel dm);
    // 서버 정보 조회
    public DataModel getServerInfo(String serviceCd);
    // 로그에서 발견된 키워드 카운팅
    public int updateKeywordCnt(List<DataModel> list);
    // 로그 히스토리 업데이트
    public int insLogHistory(DataModel dm);
    // 유저정보 조회
    public List<DataModel> getUserInfoList();
    // 코드 테이블 조회
    public DataModel getCodeList(DataModel dm);
    // 수신받은 서버에 조회할 권한을 가진 유저 조회
    public List<DataModel> getAuthUserList(DataModel dm);
    // 메세지 전송 로그 히스토리 업데이트
    public int insSMSSendHistory(DataModel dm);
}
