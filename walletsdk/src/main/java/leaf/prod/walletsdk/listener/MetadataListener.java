package leaf.prod.walletsdk.listener;

import android.util.Log;

import leaf.prod.walletsdk.model.response.relay.MetadataResult;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-04-08 5:16 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class MetadataListener extends AbstractNoArgsListener<MetadataResult> {

	private final PublishSubject<MetadataResult> subject = PublishSubject.create();

	@Override
	protected void registerEventHandler() {
		socket.on("metadata_res", objects -> {
			MetadataResult metadataResult = gson.fromJson(extractPayload(objects), MetadataResult.class);
			Log.d("", metadataResult.toString());
			subject.onNext(metadataResult);
		});
		socket.on("metadata_end", data -> subject.onCompleted());
	}

	@Override
	public Observable<MetadataResult> start() {
		return subject;
	}

	@Override
	public void stop() {
		socket.emit("metadata_end", null, args -> subject.onCompleted());
	}

	@Override
	public void send() {
	}
}
