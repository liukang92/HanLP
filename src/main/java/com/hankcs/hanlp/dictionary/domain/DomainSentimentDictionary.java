package com.hankcs.hanlp.dictionary.domain;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.CoreSentimentDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * @author liukang
 * @since 2016/3/29
 */
public class DomainSentimentDictionary {
	private static final String FILE_SENTIMENT = "sentiment.txt";
	private static Map<String, Map<String, Integer>> dict;

	public static boolean contains(String domain, String word) {
		if (dict == null){
			load(HanLP.Config.DomainSentimentDictionaryPath + FILE_SENTIMENT);
		}
		return dict.containsKey(domain) && dict.get(domain).containsKey(word);
	}

	public static int get(String domain, String word) {
		if (contains(domain, word)) {
			return dict.get(domain).get(word);
		}
		Integer sentiment = CoreSentimentDictionary.get(word);
		return sentiment == 3 ? 0 : sentiment;
	}

	private static boolean load(String fn) {
		dict = new ConcurrentHashMap<>();
		String line = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(IOUtil.getInputStream(fn), "UTF-8"));
			while ((line = bw.readLine()) != null) {
				String[] args = line.split("\t");
				Map<String, Integer> sentiments = dict.get(args[0]);
				if (sentiments == null){
					sentiments = new ConcurrentHashMap<>();
					dict.put(args[0], sentiments);
				}
				sentiments.put(args[1], Integer.valueOf(args[2]));
			}
			bw.close();
		} catch (Exception e) {
			logger.warning("读取" + fn + "失败，可能由行" + line + "造成");
			return false;
		}
		return true;
	}
}
