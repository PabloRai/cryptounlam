package unlam.crypto.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class JsonUtils {
    private static ObjectMapper MAPPER = new ObjectMapper()
                                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                                                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

    public static String getJSONString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot parse object", e);
        }
    }

}
