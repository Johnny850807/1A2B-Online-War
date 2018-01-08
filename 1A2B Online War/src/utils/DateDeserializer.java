package utils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import container.core.Constants;

public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();

        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATEFORMAT, Locale.TAIWAN);  //2017-11-24T00:00:00
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            System.err.println("Failed to parse Date due to:" + e);
            return null;
        }
    }
}