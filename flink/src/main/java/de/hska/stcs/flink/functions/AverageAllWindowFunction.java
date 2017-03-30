package de.hska.stcs.flink.functions;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created by patrickwiener on 30.03.17.
 */
public class AverageAllWindowFunction implements AllWindowFunction<Tuple3<Double, String, Float>, Tuple3<Double, String, Float>, TimeWindow> {

    @Override
    public void apply(TimeWindow timeWindow, Iterable<Tuple3<Double, String, Float>> tuple, Collector<Tuple3<Double, String, Float>> out) throws Exception {

        int sum = 0;
        String field = "";
        int counter = 0;
        float avg;
        double timestamp = 0.0;

        for (Tuple3<Double, String, Float> t: tuple) {
            timestamp = t.f0;
            field = t.f1;
            sum += t.f2;
            counter++;
        }
        avg = (float) sum / counter;

        out.collect(new Tuple3<>(timestamp, field, avg));
    }
}
