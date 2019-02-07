package com.myapp.senier.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.common.utils.KMPSearch;
import com.myapp.senier.common.utils.PushMessage;
import com.myapp.senier.common.utils.TimeUtil;
import com.myapp.senier.model.DataModel;
import com.myapp.senier.persistence.JobMapper;
import com.myapp.senier.service.LogAnalysisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

@Service("LogAnalysisService")
public class LogAnalysisServiceImpl implements LogAnalysisService {
    private final Logger logger = LoggerFactory.getLogger(LogAnalysisServiceImpl.class);

    @Autowired
    private JobMapper jobMapper;
    
    @Override
    public DataModel getServerInfo(String serviceCd) {
        return jobMapper.getServerInfo(serviceCd);
    }

    // 키워드와 로그 데이터 비교. 형태소 db 저장, 로그 히스토리 저장. 로그 분석 결과 리턴.
    @Transactional
    @Override
    public DataModel executeLogAnalyzer(DataModel logMap) throws Exception {
        DataModel resultMap = new DataModel();
        String message = logMap.getStrNull("message"), serviceCd = logMap.getStrNull("serviceCd"), logType = "";
        // 키워드 테이블의 전체 데이터
        List<DataModel> fullKeyList = jobMapper.getKeywordList(serviceCd);
        // 키워드 데이터 ( 검색에 용이하게 쓰기위해 키워드만 따로 추출 )
        List<String> onlyKeyList = jobMapper.getOnlyKeywords(serviceCd);
        // 키워드 로그 카운팅용 리스트
        List<DataModel> logCntList = new ArrayList<>();
        // 로그 타이틀
        String title = splitTitle(message);
        List<String> parsedList = new ArrayList<>();

        logger.info("executeLogAnalyzer keyList - {}", onlyKeyList);
        boolean isCritical = false; 

        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        
        String taggedString = tagger.tagString(message);
        logger.info("executeLogAnalyzer Parsed Log data - {}", taggedString);

        StringTokenizer tok = new StringTokenizer(taggedString, " ");

        while(tok.hasMoreTokens()) {
            String tokken = tok.nextToken();
            String pos = tokken.replaceAll("^.*_", "");
            String word = tokken.replaceAll("_.*$", "");

            if(pos.matches("VBP") ||
                pos.matches("PRP") ||
                pos.matches("DT") ||
                pos.matches("WP") ||
                pos.matches("PDT") ||
                pos.matches("RBS") ||
                pos.matches("WDT") ||
                pos.matches(",") || 
                pos.matches(".") ||
                pos.matches("POS") ||
                pos.matches("MD")
            ) {
                continue;
            }

            // 형용사, 명사, 부사, 동사, 전치사, 기수(숫자), 접속사
            if(pos.matches("JJ.*") ||
                pos.matches("NN.*") ||
                pos.matches("RB.*") ||
                pos.matches("VB.*") || 
                pos.matches("IN") ||
                pos.matches("CD.*") ||
                pos.matches("CC.*")
            ) {
                logger.info("검사한 단어 - {}", word);
                parsedList.add(word);
                
                // 키워드 체크
                int keyIndex = onlyKeyList.indexOf(word.toLowerCase());
                // 키워드 발견
                if(keyIndex > -1) {
                    // 키워드 데이터 get
                    DataModel keywordData = fullKeyList.get(keyIndex);
                    // 발견된 키워드 수집 
                    logCntList.add(keywordData);
                    // 키워드 처리 방법 체크
                    String excepAttr = keywordData.getStrNull("exceptionAttr").toUpperCase();

                    // 로그 타입이면 ( ex: DISK, Database, Network, ... )
                    if(excepAttr.equals("LOG_TYPE")) {
                        logType = onlyKeyList.get(keyIndex);
                    } 
                    // 크리티컬 상태 체크
                    else if(!isCritical && excepAttr.equals("CRITICAL/LMS")) {
                        // 키워드와 일치하는 단어를 찾았을 때 키워드 가중치를 통해서 크리티컬 여부 체크
                        isCritical = jobMapper.isCritical(keywordData).equals("critical") ? true : false;
                        resultMap.putStrNull("logStatus", CommonConstant.CRITICAL);
                    }
                }

                if(word.equals("less") || word.equals("more")) {
                    
                }
            }
        }

        resultMap.putStrNull("title", title);
        resultMap.putStrNull("content", message);
        resultMap.putStrNull("serviceCd", serviceCd);
        resultMap.putStrNull("logType", logType);
        resultMap.putStrNull("logDt", TimeUtil.currentDate());
        resultMap.putStrNull("logTm", TimeUtil.currentTime());
        if(!isCritical) {
            resultMap.putStrNull("logStatus", CommonConstant.NORMAL);
        }
        
        // 키워드 로그 카운팅
        jobMapper.updateKeywordCnt(logCntList);
        // 로그 데이터 저장
        jobMapper.insLogHistory(resultMap);

        return resultMap;
    }

    private String splitTitle(String message) {
        String sep = "\n";
        List<String> result = new ArrayList<>();

        List<Integer> sepList = KMPSearch.find(message, sep);
        if(sepList.size() > 0) {
            result = new ArrayList<>(Arrays.asList(message.split(sep)));
        }

        return result.get(0);
    }

    @Transactional
    @Override
    public DataModel sendMessageToManagers(DataModel model) throws Exception {
        DataModel result = new DataModel();
        String message = model.getStrNull("content");
        model.put("codeCl", model.getStrNull("serviceCd"));
        // 코드 테이블 조회
        String serverName = jobMapper.getCodeList(model).getStrNull("description");
        model.put("serverName", serverName);
        List<DataModel> authUserList = jobMapper.getAuthUserList(model);
        logger.info("메세지 받을 관리자 목록 - {}", authUserList);

        for(DataModel user : authUserList) {
            PushMessage pm = new PushMessage();
            DataModel resMap = pm.sendLMS(user.getStrNull("telNum"), message);

            resMap.putStrNull("serviceCd", model.getStrNull("serviceCd"));
            resMap.putStrNull("sendDt", TimeUtil.currentDate());
            resMap.putStrNull("sendTm", TimeUtil.currentTime());

            if(!resMap.isEmpty()) {
                logger.info("유저ID : {}, 전화번호 : {}", user.getStrNull("uid"), user.getStrNull("telNum"));
                resMap.putStrNull("groupId", resMap.getStrNull("group_id"));
                resMap.putStrNull("successFlg", "1");
                jobMapper.insSMSSendHistory(resMap);
            } else {
                resMap.putStrNull("groupId", "NOT EXIST");
                resMap.putStrNull("successFlg", "0");
                jobMapper.insSMSSendHistory(resMap);
            }
            result.putAll(resMap);
        }

        return result;
    }
}