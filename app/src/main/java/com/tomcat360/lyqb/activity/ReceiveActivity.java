package com.tomcat360.lyqb.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vondear.rxfeature.tool.RxQRCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ReceiveActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.coin_name)
    TextView coinName;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.coin_address)
    TextView coinAddress;
    @BindView(R.id.btn_copy)
    Button btnCopy;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.app_name)
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receive);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.receive_code));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_share, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                ReceiveActivityPermissionsDispatcher.showWriteWithPermissionCheck(ReceiveActivity.this);
//                ToastUtils.toast("分享");
            }
        });
    }

    @Override
    public void initView() {
        appName.setText(getResources().getString(R.string.app_name));
        coinAddress.setText((String) SPUtils.get(this, "address", ""));
    }

    @Override
    public void initData() {
        String str = (String) SPUtils.get(this, "address", "");
        coinAddress.setText(str);

        //二维码生成方式一  推荐此方法
        RxQRCode.builder(str).
                backColor(0xFFFFFFFF).
                codeColor(0xFF000000).
                codeSide(600).
                into(ivCode);
    }

    @OnClick({R.id.btn_copy, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(coinAddress.getText());
                ToastUtils.toast("复制成功");
                break;
            case R.id.btn_save:
                break;
        }
    }


    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showWrite() {
        uShare();
//        ToastUtils.toast("jinlaile ");
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForWrite() {
        Toast.makeText(ReceiveActivity.this, "权限已拒绝", Toast.LENGTH_SHORT).show();
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForWrite() {
        Toast.makeText(ReceiveActivity.this, "权限已拒绝，不在提醒", Toast.LENGTH_SHORT).show();
    }


    private void uShare() {
        UMWeb umWeb = new UMWeb("https://m.zhaoyunlicai.com/weekPayNo");
        umWeb.setTitle("赵云理财喊您领周薪啦！");//标题
        umWeb.setThumb(new UMImage(ReceiveActivity.this, R.mipmap.icon_share));  //缩略图
        umWeb.setDescription("挑战月薪制，日薪不是梦！");//描述


        ShareAction shareAction = new ShareAction(ReceiveActivity.this);
        shareAction
                .setDisplayList(SHARE_MEDIA.QQ,
//                        SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.SINA)//传入平台
                .setCallback(umShareListener)
                .withMedia(umWeb)
                .open();


    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ReceiveActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ReceiveActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t.getMessage().contains("2008")) {//错误码
                Toast.makeText(ReceiveActivity.this, "分享失败:没有安装该应用", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ReceiveActivity.this, "分享失败:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getContext(), "取消了", Toast.LENGTH_LONG).show();

        }
    };
}
