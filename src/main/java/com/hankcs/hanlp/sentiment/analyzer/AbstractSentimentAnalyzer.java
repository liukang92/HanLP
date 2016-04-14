package com.hankcs.hanlp.sentiment.analyzer;

import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.List;

/**
 * @author liukang
 * @since 2016/3/30
 */
public abstract class AbstractSentimentAnalyzer {
	/**
	 * 提取过程，情感词典提取+特征词典提取
	 */
	public abstract List<Tuple> extractTuple(String text, String domain, String object);

	/**
	 * 对抽取结果进行处理
	 */
	public abstract float analysePolarity(String text, String domain, String object);
}
