package com.lixa.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lixa.util.OrderStringUtil;

/**
 * Created by Jyace on 2016/4/15.
 */
public class Uniontable extends Activity {
    private Button bt1,bt2;
    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinzhuo);
        str = OrderStringUtil.getDataFromIntent(getIntent());
        bt1=(Button)findViewById(R.id.button);
        bt2=(Button)findViewById(R.id.button1);
        bt1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Toast.makeText(Uniontable.this,"拼桌完成",Toast.LENGTH_LONG);
                AlertDialog.Builder b = new AlertDialog.Builder(Uniontable.this);
                b.setTitle("设置成功").setMessage("您的信息已经设置成功！").setIcon(R.drawable.alert_ok)
                        .setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Uniontable.this, OrderMainMenu.class);

                        OrderStringUtil.putDataIntoIntent(i, str);

                        //      OrderStringUtil.putDataIntoIntent(i, str);
                        startActivity(i);
                    }
                }).show();

            }
        });
        bt2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Uniontable.this,OrderMainMenu.class);
                OrderStringUtil.putDataIntoIntent(intent, str);

                startActivity(intent);

            }
        });
    }
}
