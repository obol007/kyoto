package piotr.obolewicz.kyotu.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomFloatSerializer extends JsonSerializer<Float> {

    @Override
    public void serialize(Float value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            BigDecimal bd = BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP);
            gen.writeNumber(bd);
        } else {
            gen.writeNull();
        }
    }
}
