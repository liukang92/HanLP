package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dictionary.CoreSentimentDictionary;
import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class DictionayBasedTupleExtractor implements ITupleExtractor {
	List<String> patterns = new ArrayList<String>() {{
		add("[特征词] 主谓关系 [情感词]");
		add("[特征词] 主谓关系 动宾关系 [情感词]");
	}};
	private static final String SEGMARK = ",|\\.|!|\\?|，|。|！|？| ";

	@Override
	public List<Tuple> extractTuple(String context) {
		List<Tuple> tuples = new LinkedList<>();
		for (String text : context.split(SEGMARK)) {
			System.out.println(HanLP.segment(text));
			CoNLLSentence sentence = HanLP.parseDependency(text);
			for (CoNLLWord sentiment : sentence) {
				System.out.println(sentiment.getRootString());
				int polarity = CoreSentimentDictionary.get(sentiment.LEMMA);
				if (polarity != 0) {
					System.out.println(sentiment.FOLLOW);
					for (CoNLLWord object : sentiment.FOLLOW) {
						if (object.DEPREL.equals("主谓关系")) {
							tuples.add(new Tuple(object.LEMMA, sentiment.LEMMA, polarity));
						}
					}
					if (sentiment.DEPREL.equals("动宾关系")) {
						for (CoNLLWord object : sentiment.HEAD.FOLLOW) {
							if (object.DEPREL.equals("主谓关系")) {
								tuples.add(new Tuple(object.LEMMA, sentiment.HEAD.LEMMA + sentiment.LEMMA, polarity));
							}
						}
					}
				}
			}
		}

		return tuples;
	}
}
