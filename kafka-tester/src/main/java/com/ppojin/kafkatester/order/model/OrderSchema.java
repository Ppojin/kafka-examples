package com.ppojin.kafkatester.order.model;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class OrderSchema {
    private final Schema schema;

    public OrderSchema() {
        String orderSchema = "" +
                "{" +
                "   \"type\":\"record\"," +
                "   \"name\":\"orderRecord\"," +
                "   \"fields\": [" +
                "       {\"name\":\"name\",\"type\":\"string\"}," +
                "       {\"name\":\"count\",\"type\":\"int\"}" +
                "   ]" +
                "}";

        Schema.Parser parser = new Schema.Parser();
        this.schema = parser.parse(orderSchema);
    }

    public GenericRecord getRecord (){
        return new GenericData.Record(this.schema);
    }
}
