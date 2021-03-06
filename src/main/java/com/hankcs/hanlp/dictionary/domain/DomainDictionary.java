package com.hankcs.hanlp.dictionary.domain;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * @author liukang
 * @since 2016/3/29
 */
public class DomainDictionary {
	private static final String FILE_OBJECT = "object.txt";

	private static Map<String, String> dict;

	public static boolean contains(String domain, String object) {
		if (dict == null) {
			load(HanLP.Config.DomainSentimentDictionaryPath + FILE_OBJECT);
		}
		return domain.equals(dict.get(object));
	}

	private static boolean load(String fn) {
		dict = new ConcurrentHashMap<>();
		String line = null;
		try {
			BufferedReader bw = new BufferedReader(new InputStreamReader(IOUtil.getInputStream(fn), "UTF-8"));
			while ((line = bw.readLine()) != null) {
				String[] args = line.split("\t");
				for (String object : args[1].split("\\|")){
					dict.put(object, args[0]);
				}
			}
			bw.close();
		} catch (Exception e) {
			logger.warning("读取" + fn + "失败，可能由行" + line + "造成");
			return false;
		}
		return true;
	}
}
