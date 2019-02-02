package com.myapp.senier.common.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class nlp {
    public Set<String> getDistinctWords(String sentence) {
        Set<String> result = new HashSet<>();
    
        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
    
        String taggedString = tagger.tagString(sentence);
        
        StringTokenizer tok = new StringTokenizer(taggedString, " ");

        while(tok.hasMoreTokens()) {
            String tokken = tok.nextToken();
            String pos = tokken.replaceAll("^.*_", "");
            String word = tokken.replaceAll("_.*$", "");

            if(pos.matches("VBP") ||
                pos.matches("PRP") ||
                pos.matches("DT") ||
                pos.matches("WP") ||
                pos.matches("CC") ||
                pos.matches("CD") ||
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

            if(pos.matches("JJ.*") ||
                pos.matches("NN.*") ||
                pos.matches("RB.*")
            ) {
                result.add(word);
            }
        }
        
        return result;
    }
}