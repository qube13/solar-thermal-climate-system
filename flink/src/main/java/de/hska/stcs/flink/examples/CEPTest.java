package de.hska.stcs.flink.examples;

import de.hska.stcs.flink.model.cep.PumpWarning;
import de.hska.stcs.flink.model.avro.DataRecord;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Map;

/**
 * Created by patrickwiener on 30.03.17.
 */
public class CEPTest {

    private DataStream<DataRecord> dStream;

    public CEPTest (DataStream dStream) {
        this.dStream = dStream;
    }

    public DataStream<PumpWarning> applyPumpCEP () {

        DataStream<Tuple3<Double, Integer, Integer>> pumpStream = dStream
                /**
                 * Preprocessing #1:
                 *
                 * Filters out PSOP & PSOS values with dedicated timestamp from DataRecord POJO and maps it
                 * to Tuple3<Double, Integer, Integer> object
                 */
                .map(new MapFunction<DataRecord, Tuple3<Double, Integer, Integer>>() {

                    @Override
                    public Tuple3<Double, Integer, Integer> map(DataRecord dataRecord) throws Exception {

                        Double epoch = dataRecord.getTimestamp();
                        int PSOP = (int) dataRecord.getData().get("PSOP");
                        int PSOS = (int) dataRecord.getData().get("PSOS");

                        return new Tuple3<>(epoch, PSOP, PSOS);
                    }
                })
                /**
                 * Preprocessing #2:
                 *
                 * Sliding Window of 60 sec every 20 sec
                 *
                 */
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(60), Time.seconds(1)))
                .apply(new AllWindowFunction<Tuple3<Double, Integer, Integer>, Tuple3<Double, Integer, Integer>, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow timeWindow, Iterable<Tuple3<Double, Integer, Integer>> tuple, Collector<Tuple3<Double, Integer, Integer>> out) throws Exception {
                        int sumPSOP = 0;
                        int sumPSOS = 0;
                        int counter = 0;
                        int avgPSOP;
                        int avgPSOS;
                        double timestamp = 0;

                        for (Tuple3<Double, Integer, Integer> t: tuple) {
                            sumPSOP += t.f1;
                            sumPSOS += t.f2;
                            timestamp = t.f0;
                            counter++;
                        }
                        avgPSOP = sumPSOP / counter;
                        avgPSOS = sumPSOS / counter;

                        out.collect(new Tuple3<>(timestamp, avgPSOP, avgPSOS));

                    }
                });

//        pumpStream.print();

        /**
         * Define pattern
         */
        Pattern<Tuple3<Double, Integer,Integer>, ?> pumpNOKPattern = Pattern.<Tuple3<Double,Integer,Integer>>begin("first")
                .where(evt -> (evt.f1 >= 0 && evt.f2 == 0) || (evt.f1 > 0 && evt.f2 > 0))
                .followedBy("second")
                .where(evt -> evt.f1 == 0 && evt.f2 > 0)
                .within(Time.seconds(2));

//        Pattern<Tuple3<Double, Integer,Integer>, ?> pumpIOPattern = Pattern.<Tuple3<Double,Integer,Integer>>begin("first")
//                .where(evt -> evt.f1 == 0 && evt.f2 > 0)
//                .followedBy("second")
//                .where(evt -> (evt.f1 >= 0 && evt.f2 == 0) || (evt.f1 > 0 && evt.f2 > 0))
//                .within(Time.seconds(2));

        /**
         * Apply pattern on stream
         */
        PatternStream<Tuple3<Double, Integer,Integer>> pumpNOKPatternStream = CEP.pattern(
                pumpStream,
                pumpNOKPattern
        );
//        PatternStream<Tuple3<Double, Integer,Integer>> pumpIOPatternStream = CEP.pattern(
//                pumpStream,
//                pumpIOPattern
//        );

        /**
         * Derive new output stream
         */
        DataStream<PumpWarning> pumpWarnings = pumpNOKPatternStream.select(new PatternSelectFunction<Tuple3<Double, Integer, Integer>, PumpWarning>() {

            @Override
            public PumpWarning select(Map<String, Tuple3<Double, Integer, Integer>> pattern) throws Exception {

                Tuple3<Double, Integer, Integer> first = pattern.get("first");
                Tuple3<Double, Integer, Integer> second = pattern.get("second");


                return new PumpWarning(second.f0, "avgPSOP", second.f1, "avgPSOS", second.f2, "Pumps PSOP and PSOS malfunctioning" );
            }
        });

//        DataStream<PumpIO> pumpOK = pumpIOPatternStream.select(new PatternSelectFunction<Tuple3<Double, Integer, Integer>, PumpIO>() {
//
//            @Override
//            public PumpIO select(Map<String, Tuple3<Double, Integer, Integer>> pattern) throws Exception {
//
//                Tuple3<Double, Integer, Integer> first = pattern.get("first");
//                Tuple3<Double, Integer, Integer> second = pattern.get("second");
//
//
//                return new PumpIO(second.f0, "avgPSOP", second.f1, "avgPSOS", second.f2, "Pumps PSOP and PSOS iO" );
//            }
//        });
        return pumpWarnings;
    }
}
