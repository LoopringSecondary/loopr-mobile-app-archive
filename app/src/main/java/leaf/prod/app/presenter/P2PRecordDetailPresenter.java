package leaf.prod.app.presenter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import leaf.prod.app.R;
import leaf.prod.app.activity.P2PRecordDetailActivity;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-07 11:57 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class P2PRecordDetailPresenter extends BasePresenter<P2PRecordDetailActivity> {

    private AlertDialog shareDialog;

    public P2PRecordDetailPresenter(P2PRecordDetailActivity view, Context context) {
        super(view, context);
    }

    public void showShareDialog() {
        if (shareDialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_p2p_share, null);
//            view.findViewById(R.id.tv_save)
            builder.setView(view);
            builder.setCancelable(true);
            shareDialog = builder.create();
            shareDialog.setCancelable(true);
            shareDialog.setCanceledOnTouchOutside(true);
            shareDialog.getWindow().setGravity(Gravity.CENTER);
        }
        shareDialog.show();
    }
}
