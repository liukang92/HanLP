package com.hankcs.hanlp.dictionary.domain;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * @author liukang
 * @since 2016/3/29
 */
public class DomainFeatureDictionary {
	private static final String FILE_FEATURE = "feature.txt";
	private static Map<String, Map<String, Integer>> dict;

	public static boolean contains(String domain, String word) {
		if (dict == null){
			load(HanLP.Config.DomainSentimentDictionaryPath + FILE_FEATURE);
		}
		return dict.containsKey(domain) && dict.get(domain).containsKey(word);
	}

	public static int get(String domain, String word) {
		return contains(domain, word) ? dict.get(domain).get(word) : 0;
	}

	private static boolean load(String fn) {
		dict = new ConcurrentHashMap<>();
		String line = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(IOUtil.getInputStream(fn), "UTF-8"));
			while ((line = bw.readLine()) != null) {
				String[] args = line.split("\t");
				Map<String, Integer> features = dict.get(args[0]);
				if (features == null){
					features = new ConcurrentHashMap<>();
					dict.put(args[0], features);
				}
				features.put(args[1], Integer.valueOf(args[2]));
			}
			bw.close();
		} catch (Exception e) {
			logger.warning("读取" + fn + "失败，可能由行" + line + "造成");
			return false;
		}
		return true;
	}
}
