package com.hankcs.hanlp.dictionary.domain;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.sentiment.common.SentimentUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.hankcs.hanlp.sentiment.common.SentimentUtil.*;
import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * @author liukang
 * @since 2016/3/29
 */
public class DomainExceptionDictionary {
	private static final String FILE_EXCEPTION = "exception.txt";
	private static Map<String, Set<String>> dict;

	public static boolean contains(String domain, String feature, String sentiment) {
		if (dict == null) {
			load(HanLP.Config.DomainSentimentDictionaryPath + FILE_EXCEPTION);
		}
		return dict.containsKey(domain) && dict.get(domain).contains(feature + SPLIT + sentiment);
	}

	private static boolean load(String fn) {
		dict = new ConcurrentHashMap<>();
		String line = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(IOUtil.getInputStream(fn), "UTF-8"));
			while ((line = bw.readLine()) != null) {
				String[] args = line.split("\t");
				Set<String> pairs = dict.get(args[0]);
				if (pairs == null) {
					pairs = new ConcurrentSkipListSet<>();
					dict.put(args[0], pairs);
				}
				pairs.add(args[1] + SPLIT + args[2]);
			}
			bw.close();
		} catch (Exception e) {
			logger.warning("读取" + fn + "失败，可能由行" + line + "造成");
			return false;
		}
		return true;
	}
}
