package com.hankcs.hanlp.sentiment;

import com.hankcs.hanlp.sentiment.common.Tuple;

import java.util.List;

/**
 * @author liukang
 * @since 2016/3/28
 */
public interface ITupleExtractor {
	/**
	 * 提取短语，默认domain, object
	 * @param text 文本
	 * @return 短语列表
	 */
	List<Tuple> extractTuple(String text, String domain, String object);
}
