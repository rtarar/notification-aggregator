package notification.aggregator;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;

@KafkaClient
public interface TestProducer {

    @Topic("test-topic")
    void send( @KafkaKey  String key, @Body String value);
}