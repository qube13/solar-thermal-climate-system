package de.hska.stcs.flink;

import de.hska.stcs.flink.functions.AverageAllWindowFunction;
import de.hska.stcs.flink.functions.FilterFunction;
import de.hska.stcs.flink.util.AvroDeserializationSchema;
import de.hska.stcs.flink.model.avro.DataRecord;
import org.apache.commons.lang.Validate;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by patrickwiener on 30.03.17.
 */
public class STCSFlinkJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(STCSFlinkJob.class);
    private final StreamExecutionEnvironment env;

    private Properties props;

    public void init() throws Exception {

        if (props == null) {
            props = new Properties();
            props.load(STCSFlinkJob.class.getResourceAsStream("/config.properties"));
            LOGGER.info("Properties initialized");
        }

    }

    public STCSFlinkJob() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//        env.getConfig().disableSysoutLogging();
//        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
//        env.enableCheckpointing(5000);
    }



    public void run(final String topic) throws Exception {

        Validate.notNull(topic, "Topic must not be null");
        Validate.notNull(props, "Flink properties must not be null");

        /**
         * Read all Data for a specific topic and Deserialize Avro encoded messages from Apache Kafka
         */
        DataStream<DataRecord> avroKafkaStream = env.addSource(new FlinkKafkaConsumer010<>(
                        topic,
                        new AvroDeserializationSchema<>(DataRecord.class),
                        props))
                .assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());

        /**
         * Transform the received avroKafkaStream
         */
        DataStream<Tuple3<Double, String, Float>> averageRadiationStream = avroKafkaStream
                /**
                 * Map DataRecord to Tuple3<Double, String, Float> that contains
                 * Double: timestamp
                 * String: field name
                 * Float: measurement
                 */
                .map(new FilterFunction("total_radiation"))
                /**
                 * Define a sliding window
                 */
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(60), Time.seconds(1)))
                /**
                 * Apply a custom WindowFunction to calculate the average measurements
                 * in the sliding window for the before filtered field
                 */
                .apply(new AverageAllWindowFunction());


        /**
         * Print the results to stdout
         */
        averageRadiationStream.print();


        /**
         * CEP Test
         *
         * CEPTest cepTest = new CEPTest(avroKafkaStream);
         * cepTest.applyPumpCEP().print();
         * */

        /**
         * Start the Flink StreamExecutionEnvironment
         */
        env.execute("Flink dummy example");

    }

    public static void main(String[] args) throws Exception {

        STCSFlinkJob stcsFlinkJob = new STCSFlinkJob();

        stcsFlinkJob.init();
        stcsFlinkJob.run("solar_radiation");

    }
}
