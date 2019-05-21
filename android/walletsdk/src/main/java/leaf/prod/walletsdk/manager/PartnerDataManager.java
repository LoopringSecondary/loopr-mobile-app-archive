/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-25 上午10:37
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import android.content.Context;

import leaf.prod.walletsdk.model.setting.Partner;
import leaf.prod.walletsdk.service.RelayService;
import leaf.prod.walletsdk.util.WalletUtil;

public class PartnerDataManager {

    public static final String BASE_URL = "https://upwallet.io";

    public static final String LOOPRING_ADDRESS = "0x8E63Bb7Af326de3fc6e09F4c8D54A75c6e236abA";

    private static PartnerDataManager partnerDataManager = null;

    private Context context;

    private Partner partnerTo;

    private Partner partnerFrom;

    private RelayService relayService;

    private PartnerDataManager(Context context) {
        this.context = context;
        relayService = new RelayService();
    }

    public static PartnerDataManager getInstance(Context context) {
        if (partnerDataManager == null) {
            partnerDataManager = new PartnerDataManager(context);
        }
        return partnerDataManager;
    }

    public void activatePartner() {
        //todo order
//        relayService.activateInvitation().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Partner>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        unsubscribe();
//                    }
//
//                    @Override
//                    public void onNext(Partner partner) {
//                        partnerFrom = partner;
//                        unsubscribe();
//                    }
//                });
    }

    public void createPartner() {
        String owner = WalletUtil.getCurrentAddress(context);
        //todo order
//        relayService.createPartner(owner).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Partner>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        unsubscribe();
//                    }
//
//                    @Override
//                    public void onNext(Partner partner) {
//                        partnerTo = partner;
//                        unsubscribe();
//                    }
//                });
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
            result += "?cityPartner=" + partnerTo.getCityPartner();
        }
        return result;
    }
}
