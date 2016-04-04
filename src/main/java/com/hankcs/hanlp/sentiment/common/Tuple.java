package com.hankcs.hanlp.sentiment.common;

/**
 * @author liukang
 * @since 2016/3/28
 */
public class Tuple {
	private String object;
	private String feature;
	private String sentiment;
	private Polarity polarity;

	public Tuple(String feature, String sentiment, int polarity) {
		this.feature = feature;
		this.sentiment = sentiment;
		this.polarity = Polarity.values()[polarity];
	}

	public Tuple(String object, String feature, String sentiment, int polarity) {
		this(feature, sentiment, polarity);
		this.object = object;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public Polarity getPolarity() {
		return polarity;
	}

	public void setPolarity(Polarity polarity) {
		this.polarity = polarity;
	}

	public String toString() {
		return "[O = " + object + ", F = " + feature + ", S = " + sentiment + ", P = " + polarity + "]";
	}

	public enum Polarity {
		neutral("中性"), positive("正面"), negative("负面");
		private String polarity;

		Polarity(String polarity) {
			this.polarity = polarity;
		}

		public String toString() {
			return polarity;
		}
	}
}
