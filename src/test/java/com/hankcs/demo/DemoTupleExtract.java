package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.sentiment.DictionaryBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.extractor.naturerule.FeatureDictionaryTupleExtractor;
import com.hankcs.hanlp.sentiment.extractor.naturerule.SentimentDictionaryTupleExtractor;
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
				"韩国润膏多功效，二和一洗发护发。自用款，我感觉洗后头皮特别舒服。之前用拉芳、欧莱雅一天后头皮都特别痒。",
				"我要挂了啊。寝室里满满的拉芳味。。[鄙视][鄙视][鄙视]臭死了 http://t.cn/R2dLT0i",
				"不好，有头皮屑，用康王",
				"建议京东清理一下这种不讲信用的卖家",
				"这款不是很好用阿。之前去闺蜜家用了遇见香芬洗发水后爱得不行，泡沫细腻，用量很省，味道也好闻",
				"不满意，收到的时候包装盒子是湿的，一手都是油，都不知道是不是发别人退货的给我，太失望了",
				"拉芳的洗发水都是很不错的",
				"过往一期的《消费者报道》中，对海飞丝、飘柔、欧莱雅、清扬、舒蕾、拉芳等市场上常见的9款洗发水测试发现，仅有一款洗发水未标注使用MIT和CMIT!",
				"香味有点浓，香味但还不是太闷了。",
				"最佳情侣给何以未免也太讽刺了",
				"爱生活，远离拉芳[喵喵]",
				"发梢修护快点好吧么么哒",
				"之前用了感觉不怎么样 现在在用一款洛菲洗发水还不错",
				"韩寒的文字很不错",
				"我买了一瓶拉芳洗发水",
				"装修之后沃尔玛环境有很明显的改善",
				"之前用过，不太喜欢，现在用雅顺的",
		};
		try {
			List<String> lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang.txt").getFile()));
			testCase = new String[lines.size()];
			lines.toArray(testCase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DictionaryBasedTupleExtractor extractor1 = new SentimentDictionaryTupleExtractor();
		DictionaryBasedTupleExtractor extractor2 = new FeatureDictionaryTupleExtractor();
		for (String sentence : testCase) {
			if (sentence.length() > 100) {
				continue;
			}
			System.out.println(sentence);
			System.out.println(HanLP.segment(sentence));
			System.out.println("sentiment : " + extractor1.extractTuple(sentence.trim(), "洗发水", "$拉芳"));
			System.out.println("feature : " + extractor2.extractTuple(sentence.trim(), "洗发水", "拉芳"));
			System.out.println();
		}
	}
}
