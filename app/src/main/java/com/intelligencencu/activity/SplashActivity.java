package com.intelligencencu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.IsLogin;
import com.intelligencencu.utils.StreamUtils;
import com.intelligencencu.utils.ToastUntil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 这是App的闪屏页面
 */
public class SplashActivity extends AppCompatActivity {

    protected static final int CODE_UPDATE_DIALOG = 0;
    protected static final int CODE_URL_ERROR = 1;
    protected static final int CODE_IO_ERROR = 2;
    protected static final int CODE_JSON_ERROR = 3;
    protected static final int CODE_ENTER_HOME = 4;

    private TextView mTv_version;
    private RelativeLayout mRl_root;
    private String mversionName;
    private int mversioncode;
    private String mdescription;
    private String mdownloadUrl;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "b5c65220f6df2a21bd93032377bc8b5f");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

        initUI();
    }

    private void initUI() {
        mRl_root = (RelativeLayout) findViewById(R.id.rl_root);
        mTv_version = (TextView) findViewById(R.id.tv_version);
        mTv_version.setText("版本号：" + getVersionName());

        //每次启动检查新版本进行提示是否更新
        checkVersion();
        // 渐变的动画效果
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1f);

        alphaAnimation.setDuration(1000);//渐变多长时间

        mRl_root.startAnimation(alphaAnimation);

    }

    private Handler mhandler = new Handler() {
        // 重写handleMessage方法
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
                            .show();
                    beginPage();
                    break;
                case CODE_IO_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
                            .show();
                    beginPage();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误",
                            Toast.LENGTH_SHORT).show();
                    beginPage();
                    break;
                case CODE_ENTER_HOME:
                    beginPage();
                    break;
            }
        }
    };

    //进入开始页面代码块
    private void beginPage() {
        if (new IsLogin().isLogin()) {
            Intent intent = new Intent(this, BeginPageActivity.class);
            startActivity(intent);
        } else {
            ToastUntil.showShortToast(this, "请先登录再使用本功能！");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        //同时把当前Activity销毁掉，避免返回这个界面
        finish();
    }

    //弹出升级对话框的方法
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + mversionName);
        builder.setMessage(mdescription);
        // builder.setCancelable(false);//不让用户取消对话框,用户体验太差，不建议使用
        // 设置监听，看用户点击确定还是取消
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            // 如果点击确定，则进行下载更新
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                download();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            // 如果点击取消，则进入主界面
            @Override
            public void onClick(DialogInterface dialog, int which) {
                beginPage();
            }
        });
        // 设置用户点击监听，当用户在对话框界面点击返回键时起作用，走这一步
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                beginPage();
            }

        });
        // 注意不要漏show方法
        builder.show();
    }

    //检查更新的的代码块
    private void checkVersion() {
        // 当网速太快时，闪屏页还没打开就已经跳转了
        final long startTime = System.currentTimeMillis();
        // 模拟器加载本机地址用10.0.2.2
        //加载更新的配置文件(bomb后端云)
        BmobQuery<com.intelligencencu.db.File> query = new BmobQuery<>();
        query.getObject("cWS5d55d", new QueryListener<com.intelligencencu.db.File>() {
            @Override
            public void done(com.intelligencencu.db.File file, BmobException e) {
                if (e == null) {
                    BmobFile bmobFile = file.getJsonFile();
                    Log.d("更新文件", "查询成功！" + bmobFile.getFilename());
                    final String fileUrl = bmobFile.getFileUrl();
                    Log.d("更新文件", "查询成功！" + fileUrl);
                    // 耗时应该用子线程去实现,进行异步加载
                    new Thread() {
                        private HttpURLConnection conn;

                        @Override
                        public void run() {
                            // obtain:获得的意思
                            // 从全局池中返回一个新的消息实例。使我们能够避免在许多情况下分配新的对象。

                            Message mess = Message.obtain();
                            Log.d("更新文件", "到了url！" + fileUrl);
                            try {
                                URL url = new URL(fileUrl);
                                conn = (HttpURLConnection) url.openConnection();
                                // 设置GET请求方法
                                conn.setRequestMethod("GET");
                                conn.setConnectTimeout(5000);// 连接超时
                                conn.setReadTimeout(5000);// 读取超时
                                conn.connect();// 连接服务器
                                if (conn.getResponseCode() == 200) {
                                    InputStream inputStream = conn.getInputStream();
                                    // 写个工具类用来处理io流
                                    String result = StreamUtils.readFromStream(inputStream);
                                    System.out.println("网络结果是：" + result);// 测试是否拿到json数据
                                    // 解析json数据
                                    JSONObject jsonObject = new JSONObject(result);
                                    mversionName = jsonObject.getString("versionName");
                                    mversioncode = jsonObject.getInt("versionCode");
                                    mdescription = jsonObject.getString("description");
                                    mdownloadUrl = jsonObject.getString("downloadUrl");
                                    // 测试是否解析成功
                                    System.out.println("版本描述：" + mdescription);

                                    // 将本地和服务器的versionCode进行比较
                                    if (mversioncode > getVersionCode()) {
                                        // 子线程没有刷新UI的权限
                                        mess.what = CODE_UPDATE_DIALOG;
                                    } else {// 没有版本更新
                                        mess.what = CODE_ENTER_HOME;
                                    }
                                }

                            } catch (MalformedURLException e) {
                                // url错误
                                mess.what = CODE_URL_ERROR;

                            } catch (IOException e) {
                                // 网络错误
                                mess.what = CODE_IO_ERROR;

                            } catch (JSONException e) {
                                // json解析失败
                                mess.what = CODE_JSON_ERROR;
                                e.printStackTrace();
                            } finally {
                                long endTime = System.currentTimeMillis();
                                long time = startTime - endTime;
                                if (time > 2000) {
                                    mhandler.sendMessage(mess);
                                    // 断开网络连接
                                    conn.disconnect();
                                } else {
                                    try {
                                        Thread.sleep(2000 - time);
                                        mhandler.sendMessage(mess);
                                        // 断开网络连接
                                        if (conn != null) {
                                            conn.disconnect();
                                        }

                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }

                    }.start();
                } else {
                    //查询失败
                    Log.d("查询更新文件失败",e.toString());
                }
            }
        });
    }

    //获取当前安装的版本号
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有找到包名的时候会走此异常
            e.printStackTrace();
        }
        return -1;
    }

    // 获取版本名称，用以显示
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有找到包名的时候会走此异常
            e.printStackTrace();
        }
        return "";
    }

    //下载更新的方法，使用xUtils框架
    private void download() {
        // 首先得判断用户有没SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            tvProgress = (TextView) findViewById(R.id.tv_progress);
            // 设置隐藏文本显示
            tvProgress.setVisibility(View.VISIBLE);
            String target = Environment.getExternalStorageDirectory()
                    + "/intelligencencu.apk";
            HttpUtils http = new HttpUtils();

            HttpHandler<File> handler = http.download(mdownloadUrl, target,
                    new RequestCallBack<File>() {
                        // 下载进度；xUtils重写的三个方法
                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            tvProgress.setText("下载进度:" + current * 100 / total
                                    + "%");
                        }

                        // 下载失败时走得步骤
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(SplashActivity.this, "下载失败!请检查设备状态", Toast.LENGTH_SHORT)
                                    .show();
                            beginPage();
                        }

                        // 下载成功时走得步骤
                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            // 当下载成功时，应该隐式启动安装apk的activity
                            // 跳转到系统下载页面
                            // 看系统app的manifest.xml文件可以找到action name
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            // 设置类型
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            // 把下载好的文件递给系统安装
                            intent.setDataAndType(Uri.fromFile(arg0.result),
                                    "application/vnd.android.package-archive");

                            // 如果用户取消安装的话,会返回结果,回调方法onActivityResult
                            startActivityForResult(intent, 0);

                        }

                    });
        } else {
            Toast.makeText(SplashActivity.this, "没有找到SD卡，请插卡后重试！", Toast.LENGTH_SHORT).show();
        }
    }

    // 与startActivityForResult对应的方法onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        beginPage();
    }
}
