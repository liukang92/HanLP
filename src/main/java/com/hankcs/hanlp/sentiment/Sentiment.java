package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.sentiment.analyzer.AbstractSentimentAnalyzer;
import com.hankcs.hanlp.sentiment.analyzer.StandardSentimentAnalyzer;
import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.List;

/**
 * @author liukang
 * @since 2016/3/30
 */
public class Sentiment {
	/**
	 * 预置分析器
	 */
	private static AbstractSentimentAnalyzer ANALYZER = new StandardSentimentAnalyzer();

	public static void setSentimentAnalyzer(AbstractSentimentAnalyzer analyzer){
		ANALYZER = analyzer;
	}

	/**
	 * 情感分析
	 *
	 * @param text 文本
	 * @return 分词结果
	 */
	public static float sentiment(String text, String domain, String object) {
		return ANALYZER.analysePolarity(text, domain, object);
	}

	/**
	 * 提取过程，情感词典提取+特征词典提取
	 */
	public static List<Tuple> extractTuple(String text, String domain, String object) {
		return ANALYZER.extractTuple(text, domain, object);
	}
}
