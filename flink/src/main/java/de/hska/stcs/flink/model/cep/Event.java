package de.hska.stcs.flink.model.cep;

/**
 * Created by patrickwiener on 30.03.17.
 */
public class Event {
    private String timestamp;

    public Event(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "timestamp='" + timestamp + '\'' +
                '}';
    }
}
