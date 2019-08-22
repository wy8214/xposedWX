package com.hwyt.xpwx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hwyt.xpwx.Core.MyEvent;
import com.hwyt.xpwx.Dao.Wx.Account;
import com.hwyt.xpwx.Dao.Wx.Chat;
import com.hwyt.xpwx.Dao.Wx.Index;
import com.hwyt.xpwx.Dao.Wx.Message;
import com.hwyt.xpwx.Services.YkSokectsService;
import com.hwyt.xpwx.log.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        App app = (App) getApplication();
        app.setContext(getApplicationContext());


        Message ct = new Message(app);
        ct.uploadReciveMessage();

        Index index = new Index(app);
        index.wxpulse();

        Chat chat = new Chat(app);
        chat.chatIndex();

        Account account = new Account(app);
        account.getAccount(new JSONObject());


        Intent it = new Intent(getApplicationContext(), YkSokectsService.class);
        it.setPackage("com.hwyt.xpwx.Services.YkSokectsService");
        this.startService(it);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyEvent noEvent) {
        /* Do something */
        switch (noEvent.getTag()){
            case ContectSuccess:
                Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_SHORT).show();
                //todo 更新数据
                break;
            case socket_disconnet:
                Toast.makeText(getApplicationContext(),"连接断开",Toast.LENGTH_SHORT).show();
                //todo 更新数据
                break;
            default:
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
