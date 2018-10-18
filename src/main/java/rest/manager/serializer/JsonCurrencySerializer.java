package rest.manager.serializer;

import java.io.IOException;
import java.text.NumberFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("rawtypes")
public class JsonCurrencySerializer extends StdSerializer<Double> {

	private static final long serialVersionUID = -6952007058388750780L;

	private NumberFormat formatter = NumberFormat.getCurrencyInstance();

	public JsonCurrencySerializer() {
		this(null);
	}

	@SuppressWarnings("unchecked")
	public JsonCurrencySerializer(Class t) {
		super(t);
	}

	@Override
	public void serialize(Double value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		gen.writeString(formatter.format(value));
	}

}