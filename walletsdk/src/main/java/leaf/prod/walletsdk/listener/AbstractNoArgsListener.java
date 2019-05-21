package leaf.prod.walletsdk.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.socket.client.Socket;
import leaf.prod.walletsdk.SDK;
import rx.Observable;

public abstract class AbstractNoArgsListener<T> {

    Socket socket;

    Gson gson = new Gson();

    AbstractNoArgsListener() {
        this.socket = SDK.getSocketClient();
        registerEventHandler();
    }

    protected JsonElement extractPayload(Object[] objects) {
        JsonObject object = gson.fromJson(((String) objects[0]), JsonObject.class);
        return object.get("data");
    }

    protected abstract void registerEventHandler();

    public abstract Observable<T> start();

    public abstract void stop();

    public abstract void send();
}
