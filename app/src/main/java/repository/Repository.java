package repository;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Repository<Entity> {
    protected static final String BASE_URL = "https://script.google.com/macros/s/AKfycbxFHJM5G11Wc4SRwl4Gh3VKun9_QzlfmFAthGI0rihrbd9maY3c3nb8XFaE020HMYQc/exec?";
    private static final Gson gson = new GsonBuilder().create();

    protected JsonObject sendRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String requestMethod = "GET";

        connection.setRequestMethod(requestMethod);

        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String response = reader.lines().collect(Collectors.joining());

        connection.disconnect();

        JsonElement jsonElement = JsonParser.parseString(response);

        return jsonElement.getAsJsonObject();
    }

    protected RepositoryResponse post(String url) throws Exception {
        JsonObject response = this.sendRequest(url);

        boolean success = response.get("success").getAsBoolean();
        String message = response.get("message").toString();

        return new RepositoryResponse(success, message);
    }

    protected List<Entity> getAll(String url, Class<Entity[]> typeClass) throws Exception {
        String memberName = "objects";

        JsonObject jsonObject = this.sendRequest(url);
        JsonArray jsonArray = jsonObject.getAsJsonArray(memberName);

        Entity[] valuesArray = gson.fromJson(jsonArray, typeClass);

        return Arrays.asList(valuesArray);
    }
}
