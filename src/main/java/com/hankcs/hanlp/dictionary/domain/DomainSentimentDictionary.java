package com.hankcs.hanlp.dictionary.domain;

import com.hankcs.hanlp.dictionary.CoreSentimentDictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liukang
 * @since 2016/3/29
 */
public class DomainSentimentDictionary {
	static Map<String, Map<String, Integer>> dict = new HashMap<>();

	static {
		dict.put("洗发水", new HashMap<String, Integer>() {{
			put("去头屑", 1);
			put("去屑", 1);
			put("给力", 1);
			put("浓", 1);
			put("很香", 1);
			put("好", 1);
			put("快", 1);
			put("很好", 1);
			put("爱", 1);
			put("说的过去", 1);
			put("回购", 1);
			put("蛮好", 1);
			put("很多", 1);
			put("便宜", 1);
			put("舒服", 1);
			put("客气", 1);
			put("行", 1);
			put("可以", 1);
			put("闷", 2);
			put("重", 2);
			put("油", 2);
			put("远离", 2);
			put("稀", 2);
			put("痒", 2);
			put("起头屑", 2);
			put("滚吧", 2);
			put("兑水", 2);
			put("三无", 2);
			put("垃圾", 2);
			put("low", 2);
			put("地摊货", 2);
			put("没落", 2);
			put("低端", 2);
			put("太慢", 2);
			put("慢", 2);
			put("贵", 2);
			put("少", 2);
			put("假", 2);
			put("漏", 2);
			put("low味", 2);
			put("臭死", 2);
			put("湿", 2);
			put("破", 2);
			put("差评", 2);
			put("不讲信用", 2);
			put("丑死", 2);
			put("掉发", 2);
			put("别买", 2);
			put("尴尬", 2);
			put("难过", 2);
			put("失望", 2);
			put("差", 2);
			put("破掉", 2);
			put("挺好", 2);
			put("破损", 2);
			put("够慢", 2);
			put("一般", 0);
			put("盛典", 0);
			put("不是", 0);
			put("演唱", 0);
			put("必须", 0);
			put("才能", 0);
			put("情侣", 0);
			put("赞助", 0);
			put("起来", 0);
			put("运气", 0);
			put("功效", 0);
		}});
	}

	public static boolean contains(String domain, String word) {
		return dict.containsKey(domain) && dict.get(domain).containsKey(word);
	}

	public static int get(String domain, String word) {
		if (contains(domain, word)) {
			return dict.get(domain).get(word);
		}
		Integer sentiment = CoreSentimentDictionary.get(word);
		return sentiment == 3 ? 0 : sentiment;
	}
}
