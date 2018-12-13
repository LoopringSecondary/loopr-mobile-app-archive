/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-13 2:40 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.web3j.crypto.WalletUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.QRCodeUitl;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.Contact;
import leaf.prod.walletsdk.model.QRCodeType;
import leaf.prod.walletsdk.model.UserConfig;
import leaf.prod.walletsdk.util.ChineseCharUtil;
import leaf.prod.walletsdk.util.StringUtils;

public class AddContactActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.contact_name)
    MaterialEditText contactName;

    @BindView(R.id.contact_address)
    MaterialEditText contactAddress;

    @BindView(R.id.contact_note)
    MaterialEditText contactNote;

    private static int REQUEST_CODE = 1;

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitle() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        title.setBTitle(getResources().getString(R.string.add_contact));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_scan, button -> {
            Intent intent = new Intent(AddContactActivity.this, ActivityScanerCode.class);
            intent.putExtra("restrict", QRCodeType.TRANSFER.name());
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
    }

    /**
     * 0
     * 初始化数据
     */
    @Override
    public void initData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        loginDataManager = LoginDataManager.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                if (QRCodeUitl.getQRCodeType(result) == QRCodeType.TRANSFER) {
                    contactAddress.setText(result);
                } else {
                    RxToast.error(getString(R.string.qr_addr_error_tip));
                }
            }
        }
    }

    @OnClick({R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                if (StringUtils.isEmpty(contactName.getText().toString().trim())) {
                    RxToast.error(getString(R.string.input_valid_name));
                    break;
                }
                if (!WalletUtils.isValidAddress(contactAddress.getText().toString().trim())) {
                    RxToast.error(getString(R.string.input_valid_address));
                    break;
                }
                if (mergeContact()) {
                    finish();
                }
                break;
        }
    }

    private boolean mergeContact() {
        Contact contact = Contact.builder()
                .name(contactName.getText().toString().trim())
                .address(contactAddress.getText().toString().toLowerCase().trim())
                .note(contactNote.getText().toString().trim())
                .tag(ChineseCharUtil.getFirstLetter(contactName.getText().toString().trim()))
                .build();
        UserConfig userConfig = loginDataManager.getLocalUser();
        if (userConfig == null) {
            userConfig = UserConfig.builder().contacts(new ArrayList<>()).build();
        }
        List<Contact> contacts = userConfig.getContacts();
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        if (contacts.contains(contact)) {
            RxToast.error(getString(R.string.contact_duplication));
            return false;
        }
        contacts.add(contact);
        userConfig.setContacts(contacts);
        loginDataManager.updateRemote(userConfig);
        return true;
    }
}
