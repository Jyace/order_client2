package com.lixa.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OrderWeatherHandler extends DefaultHandler{
	/**
	 * 当前天气情况
	 */
	private CurrentCondition currentCondition;

	/**
	 * 某一天的天气情况
	 */
	private ForecastCondition  forecastCondition;

	/**
	 * 天气列表
	 */
	private List<ForecastCondition> forecastList;

	private boolean isCurrent = false;;

	/**
	 * 获取当前天气实体
	 * @return CurrentCondition
	 */
	public CurrentCondition getCurrentCondition() {
		return currentCondition;
	}

	/**
	 * 获取天气列表
	 * @return List<ForecastCondition>
	 */
	public List<ForecastCondition> getForecastList() {
		return forecastList;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 如果当前分析完毕，则设为假值，进入ForecaseCondition解析
		if("current_conditions".equals(localName))
			isCurrent = false;

		// 如果某一天分析完毕，则加入到List中
		if("forecast_conditions".equals(localName))
			forecastList.add(forecastCondition);

	}

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {

		if("current_conditions".equals(localName))
			isCurrent = true;
		if("forecast_conditions".equals(localName))
			forecastCondition = new ForecastCondition();

		String value = attributes.getValue("data");

		if(isCurrent){ // 当前天气

			if("temp_c".equals(localName))
				currentCondition.setTemp_c(value);
			else if("condition".equals(localName))
				currentCondition.setCondition(value);
			else if("humidity".equals(localName))
				currentCondition.setHumidity(value);
			else if("icon".equals(localName))
				currentCondition.setIcon(value);
			else if("wind_condition".equals(localName))
				currentCondition.setWind_condition(value);

		}else{ // 天气列表

			if("day_of_week".equals(localName))
				forecastCondition.setDay_of_week(value);
			else if("low".equals(localName))
				forecastCondition.setLow(value);
			else if("high".equals(localName))
				forecastCondition.setHigh(value);
			else if("icon".equals(localName))
				forecastCondition.setIcon(value);
			else if("condition".equals(localName))
				forecastCondition.setCondition(value);

		}

	}

	@Override
	public void startDocument() throws SAXException {
		forecastList = new ArrayList<ForecastCondition>();
		currentCondition = new CurrentCondition();
	}


}
