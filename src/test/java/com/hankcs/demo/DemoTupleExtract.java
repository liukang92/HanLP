package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.sentiment.DictionaryBasedTupleExtractor;
import com.hankcs.hanlp.sentiment.common.SentimentUtil;
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
	public static void testSentimentAnalysis() {
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang.txt").getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String sentence : lines) {
				String text = sentence.split("//")[0];
//				System.out.println(text);
//				System.out.println(HanLP.segment(text));
				System.out.println(SentimentUtil.toPolarityString(HanLP.sentiment(text.trim(), "洗发水", "拉芳")));
			}
	}

	public static void testTupleExtract() {
		String[] testCase = new String[]{
				"以前从来没有头皮屑，自从用了拉芳的洗护就满头头皮屑，发现一块超级大块，有小脚拇指那么大的，其他的都比那小。以前三四天不洗头都不痒，现在洗完第二天就很痒了，怎么办",
				" 光凭前代言人是张燕这一条就足够low了好吗，别的压根不用比[doge]",
				" 好玩吗，跟个傻逼似的",
				"回复 5为1受冷风吹 :香到窒息，憋着一口气洗完头再也没有用过第二次",
				" 不爱拉芳爱张仪",
				"东西到了，漏了很多，还是很喜欢用这款洗发露，所以这点就将就啦",
				"不好，有头皮屑，用康王",
				"韩国润膏多功效，二和一洗发护发。自用款，我感觉洗后头皮特别舒服。之前用拉芳、欧莱雅一天后头皮都特别痒。",
				"我要挂了啊。寝室里满满的拉芳味。。[鄙视][鄙视][鄙视]臭死了 http://t.cn/R2dLT0i",
				"建议京东清理一下这种不讲信用的卖家",
				"这款不是很好用阿。之前去闺蜜家用了遇见香芬洗发水后爱得不行，泡沫细腻，用量很省，味道也好闻",
				"不满意，收到的时候包装盒子是湿的，一手都是油，都不知道是不是发别人退货的给我，太失望了",
				"拉芳的洗发水都是很不错的",
				"香味有点浓，香味但还不是太闷了。",
				"最佳情侣给何以未免也太讽刺了",
				"爱生活，远离拉芳[喵喵]",
				"之前用了感觉不怎么样 现在在用一款洛菲洗发水还不错",
				"装修之后沃尔玛环境有很明显的改善",
				"之前用过，不太喜欢，现在用雅顺的",
		};
//		try {
//			List<String> lines = FileUtils.readLines(new File(ClassLoader.getSystemResource("corpus/lafang.txt").getFile()));
//			testCase = new String[lines.size()];
//			lines.toArray(testCase);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		DictionaryBasedTupleExtractor extractor1 = new SentimentDictionaryTupleExtractor();
		DictionaryBasedTupleExtractor extractor2 = new FeatureDictionaryTupleExtractor();
		for (String sentence : testCase) {
			if (sentence.length() > 100) {
				continue;
			}
			System.out.println(sentence);
			System.out.println(HanLP.segment(sentence));
			System.out.println("sentiment : " + extractor1.extractTuple(sentence.trim(), "洗发水", "$拉芳"));
			System.out.println("feature : " + extractor2.extractTuple(sentence.trim(), "洗发水", "$拉芳"));
			System.out.println();
		}
	}

	public static void main(String[] args) {
//		testSentimentAnalysis();
		testTupleExtract();
	}
}
