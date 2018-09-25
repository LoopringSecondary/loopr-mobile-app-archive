/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-25 上午10:37
 * Cooperation: loopring.org 路印协议基金会
 */
package com.tomcat360.lyqb.manager;

import android.content.Context;

import com.lyqb.walletsdk.model.Partner;
import com.lyqb.walletsdk.service.LoopringService;
import com.tomcat360.lyqb.utils.SPUtils;

import rx.android.schedulers.AndroidSchedulers;

public class PartnerDataManager {

    private Context context;

    private Partner partnerTo;

    private Partner partnerFrom;

    private LoopringService loopringService;

    private static PartnerDataManager partnerDataManager = null;

    public static final String BASE_URL = "https://upwallet.io";

    public static final String LOOPRING_ADDRESS = "0x8E63Bb7Af326de3fc6e09F4c8D54A75c6e236abA";

    public static PartnerDataManager getInstance(Context context) {
        if (partnerDataManager == null) {
            partnerDataManager = new PartnerDataManager(context);
        }
        return partnerDataManager;
    }

    private PartnerDataManager(Context context) {
        this.context = context;
        loopringService = new LoopringService();
    }

    public void activatePartner() {
        loopringService.activateInvitation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(partner -> partnerFrom = partner);
    }

    public void createPartner() {
        String owner = (String) SPUtils.get(context, "address", "");
        loopringService.createPartner(owner)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(partner -> partnerTo = partner);
    }

    public String getWalletAddress() {
        String result = LOOPRING_ADDRESS;
        if (partnerFrom != null && !partnerFrom.getWalletAddress().isEmpty()) {
            result = partnerFrom.getWalletAddress();
        }
        return result;
    }

    public String generateUrl() {
        String result = BASE_URL;
        if (partnerTo != null) {
            result += "?cityPartner=" + partnerTo.getPartner();
        }
        return result;
    }
}
