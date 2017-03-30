package de.hska.stcs.flink.functions;

import de.hska.stcs.flink.model.avro.DataRecord;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * Created by patrickwiener on 30.03.17.
 */
public class FilterFunction implements MapFunction<DataRecord, Tuple3<Double, String, Float>> {

    private String field;

    public FilterFunction (String field) {
        this.field = field;
    }

    @Override
    public Tuple3<Double, String, Float> map(DataRecord dataRecord) throws Exception {

        Double tstamp = dataRecord.getTimestamp();
        Float value = (Float) dataRecord.getData().get(field);

        return new Tuple3<Double, String, Float>(tstamp, field, value);
    }
}

