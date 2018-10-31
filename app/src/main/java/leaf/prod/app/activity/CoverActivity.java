package leaf.prod.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.ThirdUserUtil;

public class CoverActivity extends BaseActivity {

    @BindView(R.id.ll_import)
    LinearLayout rlImport;

    @BindView(R.id.ll_generate)
    LinearLayout rlGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cover);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        initPermissions();
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initPresenter() {
    }

    @OnClick({R.id.ll_import, R.id.ll_generate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_import:
                getOperation().forward(ImportWalletActivity.class);
                break;
            case R.id.ll_generate:
                getOperation().forward(GenerateWalletActivity.class);
                break;
        }
    }

    private void initPermissions() {
        /**
         //		 * 6.0系统 获取权限
         //		 */
        List<String> list = new ArrayList<>();
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            list.add(Manifest.permission.CAMERA);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)) {
            list.add(Manifest.permission.READ_CONTACTS);
        }
        //        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)) {
        //            list.add(Manifest.permission.CALL_PHONE);
        //        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_LOGS)) {
            list.add(Manifest.permission.READ_LOGS);
        }
        //        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
        //            list.add(Manifest.permission.READ_PHONE_STATE);
        //        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SET_DEBUG_APP)) {
            list.add(Manifest.permission.SET_DEBUG_APP);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            list.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)) {
            list.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
        if (list.size() > 0) {
            String[] mPermissionList = list.toArray(new String[]{});
            ActivityCompat.requestPermissions(this, mPermissionList, 100);
        }
    }

    @Override
    public void onBackPressed() {
        if (ThirdUserUtil.getThirdLoginUserBean(CoverActivity.this) == null) {
            getOperation().forwardClearTop(ThirdLoginActivity.class);
        }
    }
}
