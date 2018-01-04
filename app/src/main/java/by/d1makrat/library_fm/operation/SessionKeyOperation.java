package by.d1makrat.library_fm.operation;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;

public class SessionKeyOperation implements IOperation<String> {

    private String username;
    private String password;

    public SessionKeyOperation(String pUsername, String pPassword) {
        username = pUsername;
        password = pPassword;
    }

    @Override
    public String perform() throws Exception {
        URL apiRequestUrl;
        String sessionKey = null;

        //TODO move logic to sync operation
        UrlConstructor urlConstructor = new UrlConstructor();
        apiRequestUrl = urlConstructor.constructGetSessionKeyApiRequestUrl(username, password);

        HttpsClient httpsClient = new HttpsClient();
        String response = httpsClient.request(apiRequestUrl, "POST");

        JsonParser jsonParser = new JsonParser();

        String errorOrNot = jsonParser.checkForApiErrors(response);
        if (!errorOrNot.equals("No error"))
            throw new APIException(errorOrNot);
        else
            return jsonParser.parseSessionkey(response);
    }
}
