package com.hord.docusign;

import com.docusign.esign.model.UserInformationList;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class DSTokenHandler {

    private static final DSTokenHandler instance = new DSTokenHandler();
    private static final DSUserTokenHolder tokenHolder = DSUserTokenHolder.getInstance();

    private DSTokenHandler() {
    }

    public static DSTokenHandler getInstance() {
        return instance;
    }

    public String getBearerToken(String accountId, String email) throws Exception {
        String userId = getUserIdByEmail(accountId, email);
        return getBearerToken(userId);
    }

    private String getUserIdByEmail(String accountId, String email) throws Exception {
        String bearerToken = getBearerToken(Config.getDocusignBaseUserId());
        String uri = Config.getBaseUrl() + "/v2.1/accounts/" + accountId + "/users";
        Response response = ClientBuilder.newClient()
                .target(uri).queryParam("email", email)
                .request().header("Authorization", bearerToken)
                .get();
        return response
                .readEntity(new GenericType<UserInformationList>() {})
                .getUsers().stream()
                .findAny()
                .orElseThrow(() -> new Exception("Can't find user with Email: " + email + " in Account with ID: " + accountId))
                .getUserId();
    }

    public String getBearerToken(String userId) throws Exception {
        return "Bearer " + getDSToken(userId).getToken();
    }

    private DSToken getDSToken(String userId) throws Exception {
        DSToken dsToken;

        Optional<DSToken> dsTokenOpt = tokenHolder.getToken(userId);
        if (dsTokenOpt.isPresent()) {
            dsToken = dsTokenOpt.get();
            if (dsToken.isExpired())
                dsToken.refresh();
        } else {
            dsToken = new DSToken(userId);
            tokenHolder.set(userId, dsToken);
        }

        return dsToken;
    }
}
