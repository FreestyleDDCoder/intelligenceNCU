package com.intelligencencu.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RadioButton;
import android.widget.Toast;

import com.intelligencencu.db.User;
import com.intelligencencu.intelligencencu.R;
import com.intelligencencu.utils.CacheUtils;
import com.intelligencencu.utils.MD5Utils;
import com.intelligencencu.utils.ToastUntil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import dym.unique.com.springinglayoutlibrary.handler.SpringTouchRippleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingAlphaShowHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingNotificationRotateHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchDragHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchPointHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTouchScaleHandler;
import dym.unique.com.springinglayoutlibrary.handler.SpringingTranslationShowHandler;
import dym.unique.com.springinglayoutlibrary.view.SpringingEditText;
import dym.unique.com.springinglayoutlibrary.view.SpringingImageView;
import dym.unique.com.springinglayoutlibrary.view.SpringingTextView;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingLinearLayout;
import dym.unique.com.springinglayoutlibrary.viewgroup.SpringingRelativeLayout;

/**
 * Created by liangzhan on 17-3-22.
 * 登陆界面
 */

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private SpringingImageView mSimg_back;
    private SpringingImageView mSimg_avatarMan;
    private SpringingEditText mSedt_account;
    private SpringingEditText mSedt_emailHint;
    private SpringingEditText mSedt_password;
    private RadioButton mBoys;
    private RadioButton mGirls;
    private SpringingTextView mStv_regist;
    private SpringingRelativeLayout mSrl_actionBar;
    private SpringingLinearLayout mSll_mainContainer;
    private boolean boys = true;
    private Uri tempUri;
    String dateTime;
    private String uri;
    private Bitmap photo;
    private SpringingTextView stv_setimage;
    private String emailHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        dateTime = new Date(System.currentTimeMillis()).getTime() + "";
        initUI();
        initSpringLayout();
        initEvent();
        showViews();
    }

    private void showViews() {
        new SpringingAlphaShowHandler(this, mSll_mainContainer).showChildrenSequence(500, 100);
        new SpringingTranslationShowHandler(this, mSll_mainContainer).showChildrenSequence(500, 100);
        new SpringingNotificationRotateHandler(this, mSimg_avatarMan).start(1);
    }

    private void initEvent() {
        mSimg_back.setOnClickListener(this);
        mSimg_avatarMan.setOnClickListener(this);
        mStv_regist.setOnClickListener(this);
        mBoys.setOnClickListener(this);
        mGirls.setOnClickListener(this);
    }

    private void initSpringLayout() {
        mSrl_actionBar.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mSrl_actionBar).setOnlyOnChildren(true, mSimg_back));
        mSimg_back.getSpringingHandlerController().addSpringingHandler(new SpringingTouchPointHandler(this, mSimg_back).setAngle(SpringingTouchPointHandler.ANGLE_LEFT));
        mSll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringingTouchDragHandler(this, mSll_mainContainer).setBackInterpolator(new OvershootInterpolator()).setBackDuration(SpringingTouchDragHandler.DURATION_LONG).setDirection(SpringingTouchDragHandler.DIRECTOR_BOTTOM | SpringingTouchDragHandler.DIRECTOR_TOP).setMinDistance(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        mSll_mainContainer.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mSll_mainContainer).setOnlyOnChildren(true, mSedt_account, mSedt_password));
        mSimg_avatarMan.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(this, mSimg_avatarMan));
        mStv_regist.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, mStv_regist));
        stv_setimage.getSpringingHandlerController().addSpringingHandler(new SpringTouchRippleHandler(this, stv_setimage));
    }

    private void initUI() {
        mSrl_actionBar = (SpringingRelativeLayout) findViewById(R.id.srl_actionBar2);
        mSimg_back = (SpringingImageView) findViewById(R.id.simg_back2);
        mSll_mainContainer = (SpringingLinearLayout) findViewById(R.id.sll_mainContainer2);
        mSimg_avatarMan = (SpringingImageView) findViewById(R.id.simg_avatarMan2);
        mSimg_avatarMan.setIsCircleImage(true);
        mSedt_account = (SpringingEditText) findViewById(R.id.sedt_account2);
        mSedt_emailHint = (SpringingEditText) findViewById(R.id.sedt_EmailHint);
        mSedt_password = (SpringingEditText) findViewById(R.id.sedt_password2);
        mBoys = (RadioButton) findViewById(R.id.boys);
        mGirls = (RadioButton) findViewById(R.id.girls);
        mStv_regist = (SpringingTextView) findViewById(R.id.stv_regist);
        stv_setimage = (SpringingTextView) findViewById(R.id.stv_setimage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.simg_back2:
                finish();
                break;
            //设置头像
            case R.id.simg_avatarMan2:
                //请求拍照权限
                ArrayList<String> permissionList = new ArrayList<>();
                if (ContextCompat.checkSelfPermission(RegistActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(RegistActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(RegistActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (ContextCompat.checkSelfPermission(RegistActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.READ_PHONE_STATE);
                }
                if (!permissionList.isEmpty()) {
                    String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                    ActivityCompat.requestPermissions(RegistActivity.this, permissions, 1);
                } else {
                    showChoosePicDialog();
                }
                break;
            //性别设置
            case R.id.boys:
                if (!boys) {
                    mBoys.setChecked(true);
                    mGirls.setChecked(false);
                }
                boys = true;
                break;
            case R.id.girls:
                if (boys) {
                    mBoys.setChecked(false);
                    mGirls.setChecked(true);
                }
                boys = false;
                break;
            case R.id.stv_regist:
                emailHint = mSedt_emailHint.getText().toString();
                String account = mSedt_account.getText().toString();
                String password = mSedt_password.getText().toString();
                if (TextUtils.isEmpty(emailHint)) {
                    ToastUntil.showShortToast(RegistActivity.this, "邮箱不能为空！");
                } else if (TextUtils.isEmpty(account)) {
                    ToastUntil.showShortToast(RegistActivity.this, "用户名不能为空！");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUntil.showShortToast(RegistActivity.this, "密码不能为空！");
                } else {
                    regist(emailHint, account, password);
                }
                break;
        }
    }

    //用于选择头像图片
    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
        builder.setTitle("设置头像");
        String[] item = new String[]{"选择本地图片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //选择本地图片
                    case 0:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, 0);
                        break;
                    //拍照
                    case 1:
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, 1);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void checkEmail(String emailHint) {
        final String email = emailHint;
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUntil.showLongToast(RegistActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活,方便以后重置密码。");
                    finish();
                } else {
                    ToastUntil.showShortToast(RegistActivity.this, "" + e.getMessage());
                }
            }
        });
    }

    private void regist(String emailHint, String account, String password) {
        User user = new User();
        user.setEmail(emailHint);
        user.setUsername(account);
        user.setPassword(MD5Utils.MD5Encryption(password));
        if (mBoys.isChecked()) {
            user.setSex(true);
        } else {
            user.setSex(false);
        }
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    uri = saveToSdCard(photo);
                    updateIcon(uri);
                    //注册成功
                    ToastUntil.showShortToast(RegistActivity.this, "注册成功，你现在可以使用该账号了！");
                } else {
                    ToastUntil.showShortToast(RegistActivity.this, "用户名或电子邮箱已经被另一个用户注册。");
                }
            }
        });
    }

//    private void gotoBeginActivity() {
//        Intent intent = new Intent(this, BeginPageActivity.class);
//        startActivity(intent);
//    }

    //处理选择头像返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果返回码有用，则进行下一步
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //开始裁剪图片
                case 0:
                    startPhotoZoom(data.getData());
                    break;
                case 1:
                    startPhotoZoom(tempUri);
                    break;
                case 2:
                    if (data != null) {
                        // 让刚才选择裁剪得到的图片显示在界面上
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            //得到得是压缩图
            photo = extras.getParcelable("data");
            mSimg_avatarMan.setImageBitmap(photo);
        }
    }

    private void updateIcon(String avataPath) {
        if (avataPath != null) {
            final BmobFile file = new BmobFile(new File(avataPath));

            file.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.d("", "上传成功");
                        User user = BmobUser.getCurrentUser(User.class);
                        user.setImage(file);
                        user.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                Log.d("", "更新成功");
                                checkEmail(emailHint);
                            }
                        });
                    } else {
                        Log.d("", "上传失败");
                    }
                }
            });
        }
    }


    //权限请求处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        //当所有权限都开启后才进行地图活动
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(RegistActivity.this, "必须同意所有权限才能使用本功能", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(RegistActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    public String saveToSdCard(Bitmap bitmap) {
        User user = BmobUser.getCurrentUser(User.class);
        String files = CacheUtils.getCacheDirectory(this, true, user.getUsername() + "icon") + ".jpg";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

}
