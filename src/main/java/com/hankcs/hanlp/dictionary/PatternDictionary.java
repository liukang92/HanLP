package com.hankcs.hanlp.dictionary;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * 模式匹配词典（谨慎定义，优先级最高）
 *
 * @author liukang
 * @since 2016/3/24
 */
public class PatternDictionary {
	public final static String path = HanLP.Config.PatternDictionaryPath;
	public final static Map<Pattern, CoreDictionary.Attribute> patterns = new HashMap<>();

	// 自动加载词典
	static {
		long start = System.currentTimeMillis();
		if (!load(path)) {
			System.err.printf("模式匹配词典%s加载失败\n", path);
			System.exit(-1);
		} else {
			logger.info(path + "加载成功，" + patterns.size() + "个词条，耗时" + (System.currentTimeMillis() - start) + "ms");
		}
	}

	private static boolean load(String path) {
		logger.info("开始加载:" + path);
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				String param[] = line.split("\t");
				int natureCount = (param.length - 1) / 2;
				CoreDictionary.Attribute attribute = new CoreDictionary.Attribute(natureCount);
				for (int i = 0; i < natureCount; ++i) {
					attribute.nature[i] = Enum.valueOf(Nature.class, param[1 + 2 * i]);
					attribute.frequency[i] = Integer.parseInt(param[2 + 2 * i]);
					attribute.totalFrequency += attribute.frequency[i];
				}
				patterns.put(Pattern.compile(param[0]), attribute);
			}
			br.close();
			logger.info("模式匹配词典读入词条" + patterns.size());
		} catch (FileNotFoundException e) {
			logger.warning("核心词典" + path + "不存在！" + e);
			return false;
		} catch (IOException e) {
			logger.warning("核心词典" + path + "读取错误！" + e);
			return false;
		}

		return true;
	}
}
