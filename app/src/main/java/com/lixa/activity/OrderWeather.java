package com.lixa.activity;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lixa.util.CurrentCondition;
import com.lixa.util.ForecastCondition;
import com.lixa.util.OrderStringUtil;
import com.lixa.util.OrderUrlUtil;
import com.lixa.util.OrderWeatherHandler;

public class OrderWeather extends Activity{
	private Button weatherQuery;
	private ImageView weatherView;
	private TextView weatherText;
	private Spinner weatherSpinner;

	private ImageView img01;
	private TextView text01;

	private ImageView img02;
	private TextView text02;

	private ImageView img03;
	private TextView text03;

	private ImageView img04;
	private TextView text04;

	private ProgressDialog proDlg;

	private Button go_to_main_menu;

	private OrderWeatherHandler owh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_all);

		findAllView();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, OrderStringUtil.city);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weatherSpinner.setAdapter(adapter);

		weatherQuery.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				proDlg = OrderStringUtil.createProgressDialog(OrderWeather.this, getResources().getString(R.string.pro_title),
						getResources().getString(R.string.pro_message), true, true);
				proDlg.show();

				new Thread(){
					@Override
					public void run() {
						Spinner tmp = (Spinner)findViewById(R.id.wearther_spinner_all);
						Long longIndex = tmp.getSelectedItemId();
						int index = longIndex.intValue();
						String cityCode = OrderStringUtil.cityCode[index];
						Message m = new Message();
						try {
							URL url = new URL(OrderUrlUtil.WEATHER_URL + cityCode);

							SAXParserFactory spf = SAXParserFactory.newInstance();
							SAXParser sp = spf.newSAXParser();
							XMLReader read = sp.getXMLReader();

							owh = new OrderWeatherHandler();
							read.setContentHandler(owh);

							InputStreamReader isr = new InputStreamReader(url.openStream(), "gbk");
							InputSource is = new InputSource(isr);

							read.parse(is);

							m.what = OrderStringUtil.OK;
						} catch (Exception e) {
							Log.i("exe",e.getMessage());
							m.what = OrderStringUtil.ERROR;
						}
						handler.sendMessage(m);
					}
				}.start();

			}
		});

		Bundle bundle = getIntent().getBundleExtra("data");
		final String str = bundle.getString("data");

		go_to_main_menu.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(OrderWeather.this, OrderMainMenu.class);
				Bundle b = new Bundle();
				b.putString("data", str);
				intent.putExtra("data", b);
				startActivity(intent);
			}
		});
	}


	/**
	 * 实例化组件
	 */
	private void findAllView() {
		weatherQuery = (Button)findViewById(R.id.wearther_query_all);
		weatherView = (ImageView)findViewById(R.id.wearther_view_all);
		weatherText = (TextView)findViewById(R.id.wearther_text_all);
		weatherSpinner = (Spinner)findViewById(R.id.wearther_spinner_all);

		img01 = (ImageView)findViewById(R.id.image_01);
		text01 = (TextView)findViewById(R.id.text_01);

		img02 = (ImageView)findViewById(R.id.image_02);
		text02 = (TextView)findViewById(R.id.text_02);

		img03 = (ImageView)findViewById(R.id.image_03);
		text03 = (TextView)findViewById(R.id.text_03);

		img04 = (ImageView)findViewById(R.id.image_04);
		text04 = (TextView)findViewById(R.id.text_04);

		go_to_main_menu = (Button)findViewById(R.id.go_back_to_main_menu);

	}

	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {

			proDlg.dismiss();

			switch (msg.what){
				case OrderStringUtil.OK:
					CurrentCondition cc = owh.getCurrentCondition();
					String baseUrl = OrderUrlUtil.GOOGLE_URL.substring(0, OrderUrlUtil.GOOGLE_URL.length()-1);
					weatherView.setImageBitmap(OrderStringUtil.getBitMapForStringURL(baseUrl + cc.getIcon()));
					weatherText.setText(cc.toString());

					List<ForecastCondition> list = owh.getForecastList();

					ForecastCondition f = list.get(0);
					img01.setImageBitmap(OrderStringUtil.getBitMapForStringURL(baseUrl + f.getIcon()));
					text01.setText(f.toString());
					f = list.get(1);
					img02.setImageBitmap(OrderStringUtil.getBitMapForStringURL(baseUrl + f.getIcon()));
					text02.setText(f.toString());
					f = list.get(2);
					img03.setImageBitmap(OrderStringUtil.getBitMapForStringURL(baseUrl + f.getIcon()));
					text03.setText(f.toString());
					f = list.get(3);
					img04.setImageBitmap(OrderStringUtil.getBitMapForStringURL(baseUrl + f.getIcon()));
					text04.setText(f.toString());
					go_to_main_menu.setVisibility(View.VISIBLE);
					break;
				case OrderStringUtil.ERROR:
					AlertDialog.Builder builder = new AlertDialog.Builder(OrderWeather.this);
					builder.setTitle("错误")
							.setIcon(R.drawable.alert_wanring)
							.setMessage("网络信息错误，请稍后再试！")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
					break;
			}
		}
	};
}
