package Domus.DatasetUtils;

import com.google.gson.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomGson {
    public static Gson getCustomGson()
    {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalTime.class,
                        new JsonSerializer<LocalTime>() {
                            //to serialize the time
                            @Override
                            public JsonElement serialize(LocalTime t, java.lang.reflect.Type type,
                                                         JsonSerializationContext jsc) {
                                if (t != null) {
                                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                                            .ofPattern("H:m:s");
                                    return new JsonPrimitive(t.format(dateTimeFormatter));
                                } else {
                                    return null;
                                }
                            }
                        })
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (JsonElement je,
                                                                                             java.lang.reflect.Type type, JsonDeserializationContext jdc) -> {
                    //to deserialize time
                    if (je.getAsJsonPrimitive().getAsString() != null) {
                        return LocalTime.parse(je.getAsJsonPrimitive().getAsString().trim(),
                                DateTimeFormatter.ofPattern("H:m:s"));
                    } else {
                        return null;
                    }
                }).create();
    }
}
