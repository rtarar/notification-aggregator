package notification.aggregator;


import javax.inject.Singleton;

@Singleton
public class RecordStatusReport {

    private String message = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
