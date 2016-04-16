package com.lixa.util;

/**
 * <current_conditions>
 * <condition data="多云" />
 * <temp_f data="91" />
 * <temp_c data="33" />
 * <humidity data="湿度： 59%" />
 * <icon data="/ig/images/weather/cn_cloudy.gif" />
 * <wind_condition data="风向： 南、风速：5 米/秒" />
 * </current_conditions>
 */
public class CurrentCondition {
	/**
	 * condition 天气描述
	 */
	private String condition;

	/**
	 * temp_c 当前温度
	 */
	private String temp_c;

	/**
	 * humidity 湿度
	 */
	private String humidity;

	/**
	 * icon 图片描述路径
	 */
	private String icon;

	/**
	 * wind_condition 风向，风速
	 */
	private String wind_condition;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getTemp_c() {
		return temp_c;
	}

	public void setTemp_c(String tempC) {
		temp_c = tempC;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getWind_condition() {
		return wind_condition;
	}

	public void setWind_condition(String windCondition) {
		wind_condition = windCondition;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("当前：").append(condition).append("，")
				.append(temp_c).append("°C，").append(humidity)
				.append("，").append(wind_condition);
		return buf.toString();
	}

}
