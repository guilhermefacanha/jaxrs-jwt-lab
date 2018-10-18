package rest.manager.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("rawtypes")
public class JsonDateSerializer extends StdSerializer<Date> {

	private static final long serialVersionUID = -6952007058388750780L;

	private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

	public JsonDateSerializer() {
		this(null);
	}

	@SuppressWarnings("unchecked")
	public JsonDateSerializer(Class t) {
		super(t);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		gen.writeString(formatter.format(value));
	}

}