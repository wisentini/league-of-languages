package util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StringUtil {
    public static String toJSON(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        return json;
    }

    public static String encode(String string) {
        String encodedString = URLEncoder.encode(string, StandardCharsets.UTF_8);
        return encodedString;
    }
}
