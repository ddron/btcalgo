package com.btcalgo.service.api;

import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.templates.ActiveOrder;
import com.btcalgo.service.api.templates.ActiveOrdersTemplate;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;

public class ActiveOrdersAdapter extends TypeAdapter<ActiveOrdersTemplate> {
    @Override
    public void write(JsonWriter out, ActiveOrdersTemplate value) throws IOException {
    }

    @Override
    public ActiveOrdersTemplate read(JsonReader in) throws IOException {
        ActiveOrdersTemplate result = new ActiveOrdersTemplate();
        HashMap<Long, ActiveOrder> orders = new HashMap<>();
        result.setOrders(orders);

        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.beginObject();
        while (in.hasNext()) {
            String name1 = in.nextName();
            if (name1.equals("success")) {
                result.setSuccess(in.nextInt());
            } else if (name1.equals("error")) {
                result.setError(in.nextString());
            } else if (name1.equals("return")) {
                in.beginObject();
                while (in.hasNext()) {
                    long id = Long.parseLong(in.nextName());
                    ActiveOrder order = new ActiveOrder();
                    in.beginObject();
                    while (in.hasNext()) {
                        String name2 = in.nextName();
                        //noinspection IfCanBeSwitch
                        if (name2.equals("pair")) {
                            order.setSymbol(SymbolEnum.getByValue(in.nextString()));
                        } else if (name2.equals("type")) {
                            order.setDirection(Direction.valueByApiValue(in.nextString()));
                        } else if (name2.equals("amount")) {
                            order.setAmount(in.nextDouble());
                        } else if (name2.equals("rate")) {
                            order.setRate(in.nextDouble());
                        } else if (name2.equals("timestamp_created")) {
                            order.setTimestamp_created(in.nextLong());
                        } else if (name2.equals("status")) {
                            order.setStatus(in.nextInt());
                        }
                    }
                    in.endObject();
                    orders.put(id, order);
                }
                in.endObject();
            }
        }
        in.endObject();
        return result;
    }
}
