package com.ycbjie.yclivedatabus.livebus;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ycbjie.yclivedatabus.R;
import com.ycbjie.yclivedatabus.constant.Constant;
import com.yccx.livebuslib.event.LiveDataBus;
import com.yccx.livebuslib.utils.BusLogUtils;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThirdActivity4 extends AppCompatActivity {

    private TextView tvText;
    private int sendCount = 0;
    private int receiveCount = 0;
    private int sendCount2 = 0;
    private int receiveCount2 = 0;

    private Observer<String> observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            Toast.makeText(ThirdActivity4.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third4);
        tvText = findViewById(R.id.tv_text);
        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               postValueCountTest();
            }
        });
        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postValueCountTest2();
            }
        });
        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.get().with(Constant.LIVE_BUS4).postValueDelay("test_4_data",5000);
            }
        });
        findViewById(R.id.tv_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.get().with(Constant.LIVE_BUS5).postValueInterval("test_5_data",3000, "doubi");
            }
        });
        findViewById(R.id.tv_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataBus.get().with(Constant.LIVE_BUS5).stopPostInterval("doubi");
            }
        });
        findViewById(R.id.tv_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testMessageSetBefore();
            }
        });
        findViewById(R.id.tv_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageSetBefore();
            }
        });
        findViewById(R.id.tv_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThirdActivity4.this,ThirdActivity5.class));
            }
        });
        initBus();
    }

    private void initBus() {
        LiveDataBus2.get()
                .getChannel(Constant.LIVE_BUS2, String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        receiveCount++;
                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---3-"+s+"----"+receiveCount);
                    }
                });
        LiveDataBus.get()
                .with(Constant.LIVE_BUS3, String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        receiveCount2++;
                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---3-"+s+"----"+receiveCount2);
                    }
                });
        LiveDataBus.get()
                .with(Constant.LIVE_BUS4, String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---4----"+s);
                    }
                });
        LiveDataBus.get()
                .with(Constant.LIVE_BUS5, String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---5----"+s);
                    }
                });
        LiveDataBus.get()
                .with(Constant.LIVE_BUS6, String.class)
                .observeForever(observer);
        testBeforeOnCreate();
    }


    public void postValueCountTest() {
        sendCount = 100;
        receiveCount = 0;
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < sendCount; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    LiveDataBus2.get().getChannel(Constant.LIVE_BUS2).postValue("test_1_data"+sendCount);
                }
            });
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BusLogUtils.d("sendCount: " + sendCount + " | receiveCount: " + receiveCount);
                Toast.makeText(ThirdActivity4.this, "sendCount: " + sendCount +
                        " | receiveCount: " + receiveCount, Toast.LENGTH_LONG).show();
            }
        }, 1000);
    }


    public void postValueCountTest2() {
        sendCount2 = 100;
        receiveCount2 = 0;
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < sendCount2; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    LiveDataBus.get().with(Constant.LIVE_BUS3).postValue("test_2_data"+sendCount2);
                }
            });
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BusLogUtils.d("sendCount2: " + sendCount2 + " | receiveCount2: " + receiveCount2);
                Toast.makeText(ThirdActivity4.this, "sendCount2: " + sendCount2 +
                        " | receiveCount2: " + receiveCount2, Toast.LENGTH_LONG).show();
            }
        }, 1000);
    }



    private void testBeforeOnCreate() {
        //先发出一个消息
        LiveDataBus.get().with("msg_before", String.class).setValue("msg set before");
        //然后订阅这个消息
        LiveDataBus.get()
                .with("msg_before", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Toast.makeText(ThirdActivity4.this, "6---" + s, Toast.LENGTH_SHORT).show();
                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---6----"+s);
                    }
                });
    }

    String randomKey;
    public void testMessageSetBefore() {
        //先动态生成一个key
        randomKey = "key_random_" + new Random().nextInt();
        //然后发出一个消息
        LiveDataBus.get().with(randomKey, String.class).setValue("msg set before");
        //然后订阅这个消息
//        LiveDataBus.get()
//                .with(randomKey, String.class)
//                .observe(this, new Observer<String>() {
//                    @Override
//                    public void onChanged(@Nullable String s) {
//                        Toast.makeText(ThirdActivity4.this, "7---" + s +"--"+randomKey, Toast.LENGTH_SHORT).show();
//                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---7----"+s);
//                    }
//                });
        LiveDataBus.get()
                .with(randomKey, String.class)
                .observeSticky(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        // 更新数据
                        Toast.makeText(ThirdActivity4.this, "8---" + s +"--"+randomKey, Toast.LENGTH_SHORT).show();
                        BusLogUtils.d("接收消息--ThirdActivity4------yc_bus---8----"+s);
                    }
                });
    }

    public void sendMessageSetBefore() {
        LiveDataBus.get().with(randomKey, String.class).setValue("msg set after");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataBus.get()
                .with(Constant.LIVE_BUS6, String.class)
                .removeObserver(observer);
    }
}
