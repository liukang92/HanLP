package com.hankcs.demo;

import com.hankcs.hanlp.sentiment.DictionayBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.ITupleExtractor;
import com.hankcs.hanlp.sentiment.simple.SimpleRuleTupleExtractor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class DemoTupleExtract {
	public static void main(String[] args) {
		String[] testCase = new String[]{
				"这款不是很好用阿。之前去闺蜜家用了遇见香芬洗发水后爱得不行，泡沫细腻，用量很省，味道也好闻",
				"之前用了感觉不怎么样 现在在用一款洛菲洗发水还不错",
				"韩寒的文字都是很不错的",
				"韩寒的文字很不错",
				"我买了一瓶拉芳洗发水",
				"装修之后沃尔玛环境有很明显的改善",
				"之前用过，不太喜欢，现在用雅顺的",
		};
		ITupleExtractor extractor = new SimpleRuleTupleExtractor();
//		try {
//			List<String> lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang.txt").getFile()));
//			testCase = new String[lines.size()];
//			lines.toArray(testCase);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		for (String sentence : testCase) {
			System.out.println(sentence);
			System.out.println(extractor.extractTuple(sentence.trim()));
		}
	}
}
