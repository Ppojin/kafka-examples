package com.ppojin.kafkatester.order.model;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class OrderSchema {
    private final Schema schema;

    public OrderSchema(
            @Value("${avroSchema:avroSchema/order-value.json}") String avroSchemaJson
    ) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(avroSchemaJson);

        final String orderSchema;
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            orderSchema = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Schema.Parser parser = new Schema.Parser();
        this.schema = parser.parse(orderSchema);
    }

    @Bean
    public GenericRecord getRecord() {
        return new GenericData.Record(this.schema);
    }
}
