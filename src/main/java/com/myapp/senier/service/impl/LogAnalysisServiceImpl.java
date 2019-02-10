package com.myapp.senier.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.common.utils.KMPSearch;
import com.myapp.senier.common.utils.OptionalConsumer;
import com.myapp.senier.common.utils.PushMessage;
import com.myapp.senier.common.utils.StringUtil;
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

    // 키워드와 로그 데이터 비교. 로그 히스토리 저장. 로그 분석 결과 리턴.
    @Transactional
    @Override
    public DataModel executeLogAnalyzer(DataModel logMap) throws Exception {
        DataModel resultMap = new DataModel();
        // 전달받은 로그 내용
        String message = logMap.getStrNull("message");
        // 응답 서버 코드
        String serviceCd = logMap.getStrNull("serviceCd");
        // 기본 로그 타입 ( Default : 웹요청 )
        String logType = "HTTP";
        // 키워드 테이블의 전체 데이터
        List<DataModel> fullKeyList = jobMapper.getKeywordList(serviceCd);
        // 키워드 데이터 ( 검색에 용이하게 쓰기위해 키워드만 따로 추출 )
        List<String> onlyKeyList = jobMapper.getOnlyKeywords(serviceCd);
        // 키워드 로그 카운팅용 리스트
        List<DataModel> logCntList = new ArrayList<>();
        // 로그 타이틀
        String title = StringUtil.splitSeparator(message, "\n", "title").toString();
        // 파싱된 단어 저장
        List<String> parsedList = new ArrayList<>();

        logger.info("executeLogAnalyzer keyList - {}", onlyKeyList);
        boolean isCritical = false; 

        // 포스트맨 로그의 경우 ?와 !사이에 not이 들어가면 무조건 크리티컬로 간주
        if(serviceCd.equals(CommonConstant.POSTMAN_CODE)) {
            String findStr = StringUtil.findSpecificWord(message, "\\?.*?\\!");
            List<Integer> sepList = KMPSearch.find(findStr.toLowerCase(), "not");
            if(!sepList.isEmpty()) {
                isCritical = true;
                resultMap.putStrNull("logStatus", CommonConstant.CRITICAL);
            }
        }

        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        
        String taggedString = tagger.tagString(message);
        // 자빅스의 경우 타이틀 문자열을 재분석한다.
        String taggedTitle = tagger.tagString(title);
        if(serviceCd.equals(CommonConstant.ZABBIX_CODE)) {
            resultMap.putStrNull("taggedTitle", taggedTitle);
        }

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
                logger.info("executeLogAnalyzer WORD - {} / POS - {}", word, pos);
                parsedList.add(word.toLowerCase());
                
                // 자체 서버 HTTP 에러코드 체크
                if(pos.matches("CD.*") &&
                    serviceCd.equals(CommonConstant.CHECKSERVER_CODE) && 
                    word.matches(".*[0-9]*.*") &&
                    word.length() >= 3
                ) {
                    String httpStatusCode = StringUtil.findSpecificWord(word, "[0-9]*");
                    String requestError = StringUtil.nvl(StringUtil.findSpecificWord(httpStatusCode, "^4[0-9]{2,2}"), CommonConstant.NOT_USED);
                    String serverError = StringUtil.nvl(StringUtil.findSpecificWord(httpStatusCode, "^5[0-9]{2,2}"), CommonConstant.NOT_USED);
                    
                    if(!requestError.equals(CommonConstant.NOT_USED)) {
                        logType += " " + requestError;
                        word = "4xx";
                    }

                    if(!serverError.equals(CommonConstant.NOT_USED)) {
                        logType += " " + serverError;
                        word = "5xx";
                    }
                }

                // Not supported와 같은 구문은 형태소 분석이 되면서 2단어로 분리되어서 따로 처리해줌
                if(parsedList.contains("not") && parsedList.contains("supported")) {
                    word = "Not supported";
                    parsedList.clear();
                }
                
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
        
        if(!logCntList.isEmpty()) {
            // 키워드 로그 카운팅
            jobMapper.updateKeywordCnt(logCntList);
        }
        // 로그 데이터 저장
        jobMapper.insLogHistory(resultMap);

        return resultMap;
    }

    // 자빅스 타이틀 재분석
    @Override
    public void zabbixTitleAnalyzer(DataModel logMap) throws Exception {
        // sql query parameter
        DataModel param = new DataModel();
        // 형태소 분석된 타이틀
        String taggedTitle = logMap.getStrNull("taggedTitle");
        // 자연어 태깅 키워드 정규식
        String taggedKeywordRegex = "([a-zA-Z]*_[a-zA-Z]{2,3}\\s?){1,2}";
        // 명사 찾기
        String nounRegex = "\\s([a-zA-Z]*_NN.?\\s?){1,5}";
        // 동사 정규식
        String verbRegex = "([a-zA-Z]*_VB.\\s){1,2}";
        // 품사 정규식
        String posRegex = "_[a-zA-Z]{2,3}";
        // 형용사 ( less, more 등 )
        String adjRegex = "([a-zA-Z]*_JJ.?\\s?)";
        // 전치사 찾기
        String preRegex = "([a-zA-Z]*_IN)";
        // 숫자 찾기
        String numberRegex = "\\d{1,3}_CD.*";

        // 타이틀 키워드
        String statusNm = StringUtil.findSpecificWord(taggedTitle, taggedKeywordRegex).replaceAll(posRegex, "");
        // 상태가 일어난 주체
        String subject = StringUtil.findSpecificWord(taggedTitle, nounRegex).replaceAll(posRegex, "");
        // 동사
        String verb = StringUtil.findSpecificWord(taggedTitle, verbRegex).replaceAll(posRegex, "");
        // 수치
        String number = StringUtil.findSpecificWord(taggedTitle, numberRegex).replaceAll(posRegex, "");
        // less than, more than 과 같은 비교문
        String comparison = StringUtil.findSpecificWord(taggedTitle, adjRegex + preRegex).replaceAll(posRegex, "");
        // 표현식 
        String expression = comparison + " " + number;

        param.putStrNull("serviceCd", logMap.getStrNull("serviceCd"));
        param.putStrNull("statusNm", statusNm);
        param.putStrNull("errSubject", subject);
        param.putStrNull("expression", expression);
        // select query data model
        Optional<DataModel> errStatusMap = Optional.ofNullable(jobMapper.getErrorStatus(param));
        OptionalConsumer.of(errStatusMap)
                        .ifPresent(esm -> logMap.putStrNull("logStatus", esm.getStrNull("errType")))
                        .ifNotPresent(() -> {
                            param.putStrNull("errType", logMap.getStrNull("logStatus"));
                            param.putStrNull("regDt", TimeUtil.currentDate());
                            param.putStrNull("regTm", TimeUtil.currentTime());
                            jobMapper.insNewErrorStatus(param);
                        });
    }

    // Sefilcare 로그 메세지 재조립
    @Override
    public void reassemblyLogMessage(DataModel logMap) throws Exception {
        // 로그 메세지
        String message = logMap.getStrNull("content");
        // 재구성된 메세지
        StringBuilder newMessage = new StringBuilder();
        // 패키지 경로 정규식
        String packageRegex = "([a-zA-Z]*_{0,5}\\.[a-zA-Z]*){3,10}";
        // 날짜 형식 정규식
        String dateRegex = "([a-zA-Z]{3}\\s)*(\\d*\\s)*([0-9]{2}:?\\s?)*([a-zA-Z]{3})\\+\\d*\\s\\([a-zA-Z]*\\)";
        // 버전 형식 정규식
        String versionRegex = "(?i)sefilcare.*\\([0-9]*\\)";
        // 에러 발생 라인 형식 정규식
        String lineRegex = "([a-zA-z]*\\sline\\s\\d*)";
        // 날짜, 버전정보, 에러경로, 발생 line number
        String version = StringUtil.findSpecificWord(message, versionRegex);
        String date = StringUtil.findSpecificWord(message, dateRegex);
        String packageRoute = StringUtil.findSpecificWord(message, packageRegex);
        String line = StringUtil.findSpecificWord(message, lineRegex);
            
        newMessage.append("### " + version + " / ");
        newMessage.append(date + "\n");
        newMessage.append("Fatal Error Occured!! \n");
        newMessage.append("Occured path: " + packageRoute);
        if(!line.equals("")) {
            newMessage.append("\n line : " + line);
        }
        logMap.putStrNull("content", newMessage.toString());
    }

    // 사용자에게 메세지 전송
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