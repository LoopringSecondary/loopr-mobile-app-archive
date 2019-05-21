package leaf.prod.app.activity;

import java.io.ByteArrayOutputStream;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.QRCodeUitl;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.PartnerDataManager;
import leaf.prod.walletsdk.util.WalletUtil;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ShareActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.share_info)
    TextView shareInfo;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.btn_share)
    Button shareButton;

    @BindView(R.id.share_p2p_view)
    ImageView shareP2PView;

    @BindView(R.id.share_p2p_qrcode)
    ImageView shareP2PQrcode;

    @BindView(R.id.share_p2p_layout)
    ScrollView scrollView;

    private PartnerDataManager partnerDataManager;

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
            RxToast.success(getResources().getString(R.string.share_success));
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t.getMessage().contains("2008")) {//错误码
                RxToast.error(getResources().getString(R.string.share_failed_no_app));
            } else {
                RxToast.error(getResources().getString(R.string.share_failed, t.getMessage()));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.share_title));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        address.setText(WalletUtil.getCurrentAddress(this));
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                ShareActivityPermissionsDispatcher.showWriteWithPermissionCheck(ShareActivity.this);
                break;
            default:
                break;
        }
    }

    // 单个权限
    // @NeedsPermission(Manifest.permission.CAMERA)
    // 多个权限
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showWrite() {
        uShare();
    }

    // 用户拒绝授权回调（可选）
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForWrite() {
        RxToast.error(getResources().getString(R.string.no_access));
    }

    // 用户勾选了“不再提醒”时调用（可选）
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForWrite() {
        RxToast.error(getResources().getString(R.string.no_access_no_alert));
    }

    private void uShare() {
        // todo 生成分享app
        //        UMWeb umWeb = new UMWeb("https://m.zhaoyunlicai.com/weekPayNo");
        //        UMImage umImage = new UMImage(getApplicationContext(), QRCodeUitl.createQRCodeBitmap("https://mr.baidu.com/2ev3wfk?f=cp", 300));
        Bitmap bitmap = QRCodeUitl.createQRCodeBitmap(PartnerDataManager.getInstance(this).generateUrl(), 300);
        shareP2PQrcode.setImageBitmap(bitmap);
        UMImage umImage = new UMImage(getApplicationContext(), getBitmap());
        umImage.setTitle(getResources().getString(R.string.download_share));
        //        umImage.setThumb(new UMImage(ShareActivity.this, R.mipmap.icon_share));  //缩略图
        umImage.setDescription(getResources().getString(R.string.download_share));//描述
        ShareAction shareAction = new ShareAction(ShareActivity.this);
        shareAction.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.FACEBOOK)//传入平台
                .setCallback(umShareListener).withMedia(umImage).open();
    }

    public Bitmap getBitmap() {
        int h = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas c = new Canvas(bitmap);
        scrollView.draw(c);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return bitmap;
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShareActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
