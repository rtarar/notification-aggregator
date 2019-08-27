package notification.aggregator;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Properties;

@Factory
public class Aggregator {

    public static final String STREAM_RECORD_COUNT = "record-count";
    public static final String INPUT = "arln-record-status-report";
    public static final String OUTPUT = "arln-file-status-report";
    public static final String WORD_COUNT_STORE = "arln-record-store";


    @Singleton
    @Named(STREAM_RECORD_COUNT)
    KStream<String, String> wordCountStream(ConfiguredStreamBuilder builder) {
        // set default serdes
        Properties props = builder.getConfiguration();
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStream<String, String> source = builder
                .stream(INPUT);

        KTable<String, Long> groupedByWord = source
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(Serdes.String(), Serdes.String()))
                //Store the result in a store for lookup later
                .count(Materialized.as(WORD_COUNT_STORE));

        groupedByWord
                //convert to stream
                .toStream()
                //send to output using specific serdes
                .to(OUTPUT, Produced.with(Serdes.String(), Serdes.Long()));

        return source;
    }

}
