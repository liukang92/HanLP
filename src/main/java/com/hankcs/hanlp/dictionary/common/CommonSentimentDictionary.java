package com.hankcs.hanlp.dictionary.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class CommonSentimentDictionary {
	Map<String, Integer> dict;

	public static CommonSentimentDictionary create(InputStream inputStream) {
		CommonSentimentDictionary dictionary = new CommonSentimentDictionary();
		if (dictionary.load(inputStream)) {
			return dictionary;
		}
		return null;
	}

	public boolean load(InputStream inputStream) {
		dict = new HashMap<>();
		String line = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((line = bw.readLine()) != null) {
				String[] args = line.split("\t");
				String word = args[0];
				String type = args[6];
				dict.put(word, Integer.valueOf(type));
			}
			bw.close();
		} catch (Exception e) {
			logger.warning("读取" + inputStream + "失败，可能由行" + line + "造成");
			return false;
		}
		return true;
	}

	public int get(String word){
		Integer sentiment = dict.get(word);
		return sentiment == null ? 0 : sentiment;
	}
}
