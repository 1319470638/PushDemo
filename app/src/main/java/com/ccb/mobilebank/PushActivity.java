package com.ccb.mobilebank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.*;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;
import static com.ccb.mobilebank.HuaweiPushRevicer.ACTION_TOKEN;
import static com.ccb.mobilebank.HuaweiPushRevicer.ACTION_UPDATEUI;

public class PushActivity extends AppCompatActivity implements View.OnClickListener,HuaweiPushRevicer.IPushCallback{
    private static final String TAG = "MainActivity";
    private String token;
    private TextView mLogView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.setPushActivity(this);

        findViewById(R.id.btn_gettoken).setOnClickListener(this);
        findViewById(R.id.btn_deletetoken).setOnClickListener(this);
        findViewById(R.id.btn_getpushstatus).setOnClickListener(this);
        findViewById(R.id.btn_setnormal).setOnClickListener(this);
        findViewById(R.id.btn_setnofify).setOnClickListener(this);
        findViewById(R.id.btn_agreement).setOnClickListener(this);

        //以下代码作用仅仅为了在sample界面上显示push相关信息
        HuaweiPushRevicer.registerPushCallback(this);

        HMSAgent.connect(this, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Log.e(TAG, "onCreate: HMSAgent.onConnect rst:" + rst);
            }
        });

        // 设置别名
        TextView  set_alias= findViewById(R.id.set_alias);
        set_alias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PushActivity.this);
                new AlertDialog.Builder(PushActivity.this)
                        .setTitle(R.string.set_alias)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.setAlias(PushActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 撤销别名
        findViewById(R.id.unset_alias).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PushActivity.this);
                new AlertDialog.Builder(PushActivity.this)
                        .setTitle(R.string.unset_alias)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.unsetAlias(PushActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

            }
        });
        // 设置帐号
        findViewById(R.id.set_account).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PushActivity.this);
                new AlertDialog.Builder(PushActivity.this)
                        .setTitle(R.string.set_account)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.setUserAccount(PushActivity.this, account, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();

            }
        });
        // 撤销帐号
        findViewById(R.id.unset_account).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PushActivity.this);
                new AlertDialog.Builder(PushActivity.this)
                        .setTitle(R.string.unset_account)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.unsetUserAccount(PushActivity.this, account, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 设置标签
        findViewById(R.id.subscribe_topic).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PushActivity.this);
                new AlertDialog.Builder(PushActivity.this)
                        .setTitle(R.string.subscribe_topic)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.subscribe(PushActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 撤销标签
        findViewById(R.id.unsubscribe_topic).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PushActivity.this);
                new AlertDialog.Builder(PushActivity.this)
                        .setTitle(R.string.unsubscribe_topic)
                        .setView(editText)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.unsubscribe(PushActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        // 设置接收消息时间
        findViewById(R.id.set_accept_time).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* new TimeIntervalDialog(MainActivity.this, new TimeIntervalDialog.TimeIntervalInterface() {

                    @Override
                    public void apply(int startHour, int startMin, int endHour,
                                      int endMin) {
                        MiPushClient.setAcceptTime(MainActivity.this, startHour, startMin, endHour, endMin, null);
                    }

                    @Override
                    public void cancel() {
                        //ignore
                    }

                })
                        .show();*/
            }
        });
        // 暂停推送
        findViewById(R.id.pause_push).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.pausePush(PushActivity.this, null);
            }
        });

        findViewById(R.id.resume_push).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.resumePush(PushActivity.this, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HuaweiPushRevicer.unRegisterPushCallback(this);
    }

    @Override
    public void onReceive(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (b != null && ACTION_TOKEN.equals(action)) {
                token = b.getString(ACTION_TOKEN);
                Log.e("Token123", "onReceive: " + token);
            } else if (b != null && ACTION_UPDATEUI.equals(action)) {
                String log = b.getString("log");
                showLog(log);

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gettoken:
                getToken();
                break;
            case R.id.btn_deletetoken:
                deleteToken();
                break;
            case R.id.btn_getpushstatus:
                getPushStatus();
                break;
            case R.id.btn_setnormal:
                setReceiveNormalMsg(true);
                break;
            case R.id.btn_setnofify:
                setReceiveNotifyMsg(true);
                break;
            case R.id.btn_agreement:
                showAgreement();
                break;
            default:
                break;
        }
    }

    /**
     * 获取token
     */
    private void getToken() {
        showLog("get token: begin");
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(int rtnCode) {
                showLog("get token: end code=" + rtnCode);
            }
        });
    }

    /**
     * 删除token
     */
    private void deleteToken() {
        showLog("deleteToken:begin");
        HMSAgent.Push.deleteToken(token, new DeleteTokenHandler() {
            @Override
            public void onResult(int rst) {
                showLog("deleteToken:end code=" + rst);
            }
        });
    }

    /**
     * 获取push状态
     */
    private void getPushStatus() {
        showLog("getPushState:begin");
        HMSAgent.Push.getPushState(new GetPushStateHandler() {
            @Override
            public void onResult(int rst) {
                showLog("getPushState:end code=" + rst);
            }
        });
    }

    /**
     * 设置是否接收普通透传消息
     *
     * @param enable 是否开启
     */
    private void setReceiveNormalMsg(boolean enable) {
        showLog("enableReceiveNormalMsg:begin");
        HMSAgent.Push.enableReceiveNormalMsg(enable, new EnableReceiveNormalMsgHandler() {
            @Override
            public void onResult(int rst) {
                showLog("enableReceiveNormalMsg:end code=" + rst);
            }
        });
    }

    /**
     * 设置接收通知消息
     *
     * @param enable 是否开启
     */
    private void setReceiveNotifyMsg(boolean enable) {
        showLog("enableReceiveNotifyMsg:begin");
        HMSAgent.Push.enableReceiveNotifyMsg(enable, new EnableReceiveNotifyMsgHandler() {
            @Override
            public void onResult(int rst) {
                showLog("enableReceiveNotifyMsg:end code=" + rst);
            }
        });
    }

    /**
     * 显示push协议
     */
    private void showAgreement() {
        showLog("queryAgreement:begin");
        HMSAgent.Push.queryAgreement(new QueryAgreementHandler() {
            @Override
            public void onResult(int rst) {
                showLog("queryAgreement:end code=" + rst);
            }
        });
    }

    StringBuffer sbLog = new StringBuffer();

    public void showLog(String logLine) {
        DateFormat format = new java.text.SimpleDateFormat("MMdd.hh-mm-ss.SSS", Locale.CHINA);
        String time = format.format(new Date());

        sbLog.append(time).append(":").append(logLine).append('\n');
        Log.e("showLog", "======= " + sbLog.toString());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                View vText = findViewById(R.id.log);

                if (vText != null && vText instanceof TextView) {
                    TextView tvLog = (TextView) vText;
                    tvLog.setText(sbLog.toString());
                }

                View vScroll = findViewById(R.id.sv_log);
                if (vScroll != null && vScroll instanceof ScrollView) {
                    ScrollView svLog = (ScrollView) vScroll;
                    svLog.fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }






}
