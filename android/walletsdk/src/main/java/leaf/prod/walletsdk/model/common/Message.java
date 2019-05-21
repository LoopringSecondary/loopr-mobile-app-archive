package leaf.prod.walletsdk.model.common;

import lombok.Data;

@Data
public class Message {

    private String text;

    private long time;

    private String sender;

    public Message(String text, long time, String sender) {
        this.text = text;
        this.time = time;
        this.sender = sender;
    }
}
