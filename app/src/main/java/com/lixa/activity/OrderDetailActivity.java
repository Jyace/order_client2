package com.lixa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixa.bean.Orders;
import com.lixa.provide.OrderAdapter;
import com.lixa.util.OrderHttpUtil;
import com.lixa.util.OrderStringUtil;
import com.lixa.util.OrderUrlUtil;

public class OrderDetailActivity extends Activity {

	private String id;

	private ImageView detail_image;
	private TextView detail_price;
	private TextView detail_name;
	private TextView detail_create;
	private TextView detail_desc;

	private Button make_order;
	private Button collect_order;

	private AutoCompleteTextView site_auto_text_number;

	private OrderAdapter orderDb;

	private Orders o;
	private ProgressDialog proDlg;
	private String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);

		str = OrderStringUtil.getDataFromIntent(getIntent());
		id = getIntent().getStringExtra("ID");

		proDlg = OrderStringUtil.createProgressDialog(this, "加载数据", "正在读取图片，请稍后...", true, true);
		proDlg.show();

		findAllView(); // 实例化组件
		setButtonListener(); // 添加事件 site_auto_text_numver" AutoCompleteTextView

		site_auto_text_number = (AutoCompleteTextView)findViewById(R.id.site_auto_text_numver);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderDetailActivity.this,
				android.R.layout.simple_dropdown_item_1line, OrderStringUtil.siteNumber);
		site_auto_text_number.setAdapter(adapter);

		new Thread(){
			@Override
			public void run() {
				o = new Orders();
				orderDb = new OrderAdapter(OrderDetailActivity.this);
				Cursor c = orderDb.queryOrderListById(id);
				if(c.moveToFirst()){
					do{
						int orderId_index = c.getColumnIndex(OrderAdapter.ORDER_ID);
						o.setOrderId(c.getString(orderId_index));

						int name_index = c.getColumnIndex(OrderAdapter.NAME);
						o.setName(c.getString(name_index));

						int ddesc_index = c.getColumnIndex(OrderAdapter.DESCRIPTION);
						o.setDesc(c.getString(ddesc_index));

						int price_index = c.getColumnIndex(OrderAdapter.PRICE);
						o.setPrice(c.getString(price_index));

						int image_index = c.getColumnIndex(OrderAdapter.IMAGE_PATH);
						o.setImage(c.getString(image_index));

						int collect_index = c.getColumnIndex(OrderAdapter.COLLECT);
						o.setCollect(c.getString(collect_index));

						int create_index = c.getColumnIndex(OrderAdapter.CREATE_AT);
						o.setCreate(c.getString(create_index));

						c.moveToNext();
					}while(!c.isAfterLast());
					orderDb.closeDB(c);
					Message m = new Message();
					m.what = OrderStringUtil.OK;
					handler.sendMessage(m);
				}
			}
		}.start();

	}

	/**
	 * 设置点击事件
	 */
	private void setButtonListener() {
		/**
		 * 点菜
		 */
		make_order.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
				final String did = site_auto_text_number.getText().toString().trim();
				if("".equals(did)){
					builder.setTitle("请输入座位号").setMessage("请在下面座位号输入框中输入座位号！")
							.setIcon(R.drawable.alert_wanring).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				boolean f = false;
				for(int i = 0; i < OrderStringUtil.siteNumber.length; i ++){
					if(did.equals(OrderStringUtil.siteNumber[i])){
						f = true;
						break;
					}
				}
				if(!f){
					builder.setTitle("无效").setMessage("座位号无效，请重新输入！\n有效的座位号为：001-100")
							.setIcon(R.drawable.alert_wanring).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				proDlg = OrderStringUtil.createProgressDialog(OrderDetailActivity.this, "发送数据",
						"正在发送数据到服务台，请稍后！", true, true);
				proDlg.show();
				new Thread(){
					@Override
					public void run() {
						String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.MAKE_ORDER;
						String requestString = "oid="+id+"&did="+did+"&uid="+str.split(",")[1];
						String res = OrderHttpUtil.getHttpPostResultForUrl(url + requestString).trim();

						Message m = new Message();

						if("0".equals(res))
							m.what = OrderStringUtil.MAKE_ORDER_OK;
						else if("-1".equals(res))
							m.what = OrderStringUtil.MAKE_ORDER_ERROR;
						else
							m.what = OrderStringUtil.ERROR;
						handler.sendMessage(m);
					}
				}.start();
			}
		});

		/**
		 * 收藏/取消收藏
		 */
		collect_order.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
				orderDb.openDB();
				if("-1".equals(o.getCollect())){
					int count = orderDb.collectOrder(id);
					if(count > 0){
						builder.setTitle("收藏成功").setIcon(R.drawable.alert_add);
						builder.setMessage("收藏菜单成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// 重绘。。。。。、
								o.setCollect("0");
								collect_order.setText("取消收藏");
							}
						}).show();
					}
				} else {
					int count = orderDb.disCollectOrder(id);
					if(count > 0){
						builder.setTitle("取消成功").setIcon(R.drawable.alert_ok);
						builder.setMessage("取消收藏成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// 重绘。。。。。
								o.setCollect("-1");
								collect_order.setText(" 收 藏 ");
							}
						}).show();
					}
				}
			}
		});

		/**
		 * 返回菜品列表
		 */
		findViewById(R.id.go_back_order_list_btn).setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(OrderDetailActivity.this, OrderListActivity.class);
				OrderStringUtil.putDataIntoIntent(i,str);
				startActivity(i);
			}
		});
	}

	/**
	 * 实例化组件
	 */
	private void findAllView() {
		detail_image = (ImageView)findViewById(R.id.order_detail_iamge);
		detail_price = (TextView)findViewById(R.id.order_detail_price);
		detail_name = (TextView)findViewById(R.id.order_detail_name);
		detail_create = (TextView)findViewById(R.id.order_detail_create);
		detail_desc = (TextView)findViewById(R.id.order_detail_desc);

		make_order = (Button)findViewById(R.id.mank_order_detail_btn);
		collect_order = (Button)findViewById(R.id.collect_order_detail_btn);

		site_auto_text_number = (AutoCompleteTextView)findViewById(R.id.site_auto_text_numver);
	}

	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
			proDlg.dismiss();

			switch(msg.what){
				case OrderStringUtil.OK:
					setDataToViews();
					break;
				case OrderStringUtil.MAKE_ORDER_OK:
					builder.setTitle("点菜成功").setIcon(R.drawable.alert_ok).setMessage("点菜成功，请稍后！")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent i = new Intent(OrderDetailActivity.this, OrderListActivity.class);
									OrderStringUtil.putDataIntoIntent(i,str);
									startActivity(i);
								}
							}).show();
					break;
				case OrderStringUtil.MAKE_ORDER_ERROR:
					builder.setTitle("错误").setIcon(R.drawable.alert_ok).setMessage("服务器错误，请联系服务员！")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							}).show();
					break;
				case OrderStringUtil.ERROR:
					builder.setTitle("重复操作").setIcon(R.drawable.alert_error).setMessage("您已经点过此菜了，请不要重复操作！")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
							}).show();
					break;
			}
		}
	};

	/**
	 * 设置数据
	 */
	private void setDataToViews() {
		String urlString = OrderHttpUtil.BASE_URL + o.getImage();
		Bitmap map = OrderStringUtil.getBitMapForStringURL(urlString);
		detail_image.setImageBitmap(map);

		detail_price.setText("价格：" + o.getPrice() + "元");
		detail_name.setText("菜名：" + o.getName());
		detail_create.setText("预估制作时间：" + o.getCreate());
		detail_desc.setText("简介\n    " + o.getDesc());

		if("-1".equals(o.getCollect()))
			collect_order.setText(" 收 藏 ");
		else
			collect_order.setText("取消收藏");
		collect_order.setVisibility(View.VISIBLE);

		make_order.setVisibility(View.VISIBLE);
		findViewById(R.id.go_back_order_list_btn).setVisibility(View.VISIBLE);
	}

}
