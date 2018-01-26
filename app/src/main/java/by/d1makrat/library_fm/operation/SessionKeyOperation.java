package by.d1makrat.library_fm.operation;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

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

        UrlConstructor urlConstructor = new UrlConstructor();
        apiRequestUrl = urlConstructor.constructGetSessionKeyApiRequestUrl(username, password);

        HttpsClient httpsClient = new HttpsClient();
        String response = httpsClient.request(apiRequestUrl, RequestMethod.POST);

        JsonParser jsonParser = new JsonParser();

        String errorOrNot = jsonParser.checkForApiErrors(response);
        if (!errorOrNot.equals(API_NO_ERROR))
            throw new APIException(errorOrNot);
        else
            return jsonParser.parseSessionkey(response);
    }
}