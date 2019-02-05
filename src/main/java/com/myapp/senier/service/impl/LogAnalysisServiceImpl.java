package com.myapp.senier.service.impl;

import java.util.List;
import java.util.StringTokenizer;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.model.DataModel;
import com.myapp.senier.persistence.JobMapper;
import com.myapp.senier.service.LogAnalysisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

@Service("LogAnalysisService")
public class LogAnalysisServiceImpl implements LogAnalysisService {
    private final Logger logger = LoggerFactory.getLogger(LogAnalysisServiceImpl.class);

    @Autowired
    private JobMapper jobMapper;
    
    // 키워드와 로그 데이터 비교. 형태소 db 저장, 로그 히스토리 저장. 로그 분석 결과 리턴.
    @Transactional
    @Override
    public DataModel executeLogAnalyzer(DataModel logMap) throws Exception {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        DataModel resultMap = new DataModel();
        logger.info("executeLogAnalyzer param - {}", logMap.getStrNull("serviceCd"));
        List<DataModel> keyList = jobMapper.getKeywords(logMap.getStrNull("serviceCd"));
        logger.info("executeLogAnalyzer keyList - {}", keyList);
        boolean isCritical = false; 

        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
    
        String taggedString = tagger.tagString(logMap.getStrNull("message"));
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
                pos.matches("MD") ||
                pos.matches("VBZ")
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
                // step1 : 분석처리된 단어들을 DB에 저장 ( 사용자 인터페이스에서 통계치로 사용 )
                // 형태소
                logMap.putStrNull("pos", pos);
                // 단어
                logMap.putStrNull("word", word);
                // 형태소 분석된 주요 단어들을 DB에 저장
                jobMapper.insMorpheme(logMap);

                // step2 : 키워드 테이블의 값과 비교
                if(!isCritical && keyList.contains(word)) {
                    isCritical = true;
                    resultMap.putStrNull("logStatus", CommonConstant.CRITICAL);
                }
            }
        }

        if(!isCritical) {
            resultMap.putStrNull("logStatus", CommonConstant.NORMAL);
        }

        return resultMap;
    }
}