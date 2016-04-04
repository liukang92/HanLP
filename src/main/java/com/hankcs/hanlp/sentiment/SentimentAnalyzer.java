package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.sentiment.common.Tuple;
import com.hankcs.hanlp.sentiment.extractor.naturerule.FeatureDictionaryTupleExtractor;
import com.hankcs.hanlp.sentiment.extractor.naturerule.SentimentDictionaryTupleExtractor;

import java.util.List;

/**
 * @author liukang
 * @since 2016/3/30
 */
public class SentimentAnalyzer {
	/**
	 * 预置分词器
	 */
	public static final SentimentAnalyzer ANALYZER = new SentimentAnalyzer();

	/**
	 * 分词
	 *
	 * @param text 文本
	 * @return 分词结果
	 */
	public static int sentiment(String text, String domain, String object) {
		return ANALYZER.analysePolarity(text, domain, object);
	}

	protected List<Tuple> extract(String text, String domain, String object) {
		DictionaryBasedTupleExtractor extractor = new SentimentDictionaryTupleExtractor();
		List<Tuple> tuples = extractor.extractTuple(text, domain, object);
		extractor = new FeatureDictionaryTupleExtractor();
		tuples.addAll(extractor.extractTuple(text, domain, object));
		return tuples;
	}

	protected int analysePolarity(String text, String domain, String object) {
		int polarity = 0;
		for (Tuple tuple : extract(text, domain, object)) {
			if (tuple.getPolarity() == Tuple.Polarity.positive) {
				polarity++;
			} else if (tuple.getPolarity() == Tuple.Polarity.negative) {
				polarity--;
			}
		}
		return polarity;
	}
}
