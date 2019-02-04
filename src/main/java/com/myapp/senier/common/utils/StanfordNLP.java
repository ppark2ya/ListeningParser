package com.myapp.senier.common.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordNLP {
    private final Logger logger = LoggerFactory.getLogger(StanfordNLP.class);

    public Set<String> getDistinctWords(String sentence) {
        logger.info("Original Log - {}", sentence);
        Set<String> result = new HashSet<>();
    
        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
    
        String taggedString = tagger.tagString(sentence);
        logger.info("Parsed Log data - {}", taggedString);

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
                result.add(word);
            }
        }
        
        return result;
    }
}