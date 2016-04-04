package com.hankcs.hanlp.dictionary.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liukang
 * @since 2016/3/29
 */
public class DomainFeatureDictionary {
	static Map<String, Map<String, Integer>> dict = new HashMap<>();

	static {
		dict.put("洗发水", new HashMap<String, Integer>() {{
			put("头皮屑", 2);
		}});
	}

	public static boolean contains(String domain, String word) {
		return dict.containsKey(domain) && dict.get(domain).containsKey(word);
	}

	public static int get(String domain, String word) {
		return contains(domain, word) ? dict.get(domain).get(word) : 0;
	}
}
