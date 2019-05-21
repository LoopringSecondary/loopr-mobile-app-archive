package leaf.prod.walletsdk.listener;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.google.gson.JsonElement;

import leaf.prod.walletsdk.model.request.relayParam.BalanceParam;
import leaf.prod.walletsdk.model.response.relay.BalanceResult;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BalanceListener extends AbstractListener<BalanceResult, BalanceParam> {

	public static final String TAG = "balance";

	private final PublishSubject<BalanceResult> subject = PublishSubject.create();

	@Override
	protected void registerEventHandler() {
		socket.on("balance_res", objects -> {
			JsonElement element = extractPayload(objects);
			BalanceResult balanceResult = gson.fromJson(element, BalanceResult.class);
			subject.onNext(balanceResult);
		});
		socket.on("balance_end", data -> {
			//            System.out.println(Arrays.toString(data));
			subject.onCompleted();
		});
	}

	@Override
	public Observable<BalanceResult> start() {
		return subject;
	}

	@Override
	public void stop() {
		socket.emit("balance_end", null, args -> subject.onCompleted());
	}

	@Override
	public void send(BalanceParam param) {
		Log.d("", "balance send===============================>");
		String json = gson.toJson(param);
		socket.emit("balance_req", json);
	}

	public void queryByOwner(List<String> addresses, List<String> tokens) {
		BalanceParam param = BalanceParam.builder()
				.paramsForAccounts(BalanceParam.ParamsForAccounts.builder().addresses(addresses).tokens(tokens).build())
				.build();
		send(param);
	}

	public void queryByOwner(String address) {
		BalanceParam param = BalanceParam.builder()
				.paramsForAccounts(BalanceParam.ParamsForAccounts.builder().addresses(Arrays.asList(address)).build())
				.build();
		send(param);
	}
}
