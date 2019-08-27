package notification.aggregator;



import io.micronaut.configuration.kafka.annotation.*;

import javax.inject.Inject;

@KafkaListener(offsetReset = OffsetReset.EARLIEST,
        offsetStrategy = OffsetStrategy.DISABLED,
        groupId = "test-listener")
public class TestListener {

    @Inject
    RecordStatusReport holder;

    @Topic(value = "test-topic")
    public void receive( String key, String value) {
        System.out.println("Received: key: " + key + ", value: " + value);

        holder.setMessage(value);
    }
}