package com.ppojin.warehouse.order;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Getter
public class OrderDTO {
    private String name;
    private Integer count;

    public static class OrderDeserializer implements Deserializer<OrderDTO> {
        ObjectMapper mapper = new ObjectMapper();

        @Override
        public OrderDTO deserialize(String topic, byte[] data) {
            try{
                return mapper.readValue(data, OrderDTO.class);
            } catch (StreamReadException e) {
                throw new RuntimeException(e);
            } catch (DatabindException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
