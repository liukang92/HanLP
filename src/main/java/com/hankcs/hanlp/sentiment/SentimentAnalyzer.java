package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.sentiment.common.Tuple;
import com.hankcs.hanlp.sentiment.extractor.naturerule.FeatureDictionaryTupleExtractor;
import com.hankcs.hanlp.sentiment.extractor.naturerule.SentimentDictionaryTupleExtractor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;

/**
 * @author liukang
 * @since 2016/3/30
 */
public class SentimentAnalyzer {
	/**
	 * 预置分析器
	 */
	public static final SentimentAnalyzer ANALYZER = new SentimentAnalyzer();

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
	protected List<Tuple> extract(String text, String domain, String object) {
		DictionaryBasedTupleExtractor extractor = new SentimentDictionaryTupleExtractor();
		List<Tuple> tuples = extractor.extractTuple(text, domain, object);
		extractor = new FeatureDictionaryTupleExtractor();
		tuples.addAll(extractor.extractTuple(text, domain, object));
		return tuples;
	}

	/**
	 * 对抽取结果进行处理
	 */
	protected float analysePolarity(String text, String domain, String object) {
		float polarity = 0;
		String defaultObject = CORE + object;
		for (Tuple tuple : extract(text, domain, defaultObject)) {
//			System.out.println(tuple);
			// 情感基数
			float base = 1;
			// 如果包含默认主体，长句则pass，断句则 * 0.8
			if (ArrayUtils.contains(tuple.getObject().split(SPLIT), defaultObject)) {
				if (text.length() > 100) {
					base = 0;
				} else {
					base *= 0.8;
				}
			// 如果不包含主体则pass
			} else if (!ArrayUtils.contains(tuple.getObject().split(SPLIT), object)) {
				base = 0;
			}
			// 若特征为空则 * 0.5
			if (tuple.getFeature() == null) {
				base *= 0.5;
			// 若特征词匹配为主体，则 * 2
			} else if (tuple.getFeature().contains(object)) {
				base = 2;
			}
			// 根据极性选择+/-
			if (tuple.getPolarity() == Tuple.Polarity.positive) {
				polarity += base;
			} else if (tuple.getPolarity() == Tuple.Polarity.negative) {
				polarity -= base;
			}
		}
		return polarity;
	}
}
