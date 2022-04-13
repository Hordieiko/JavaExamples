package com.hord.docusign.demo;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;
import com.hord.docusign.Config;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DocuSignApiClient {

    static final int MILLIS_PER_SECOND = 1000;
    private static final String DOCUSIGN_USER_ID_HORDIEIKO = "3957ff3e-5518-4b7a-9f75-99364ce70ca4";
    private static final String DOCUSIGN_USER_ID_HORDIEIKO_PLUS_1 = "2a747be7-206c-4218-8512-e2961b7a826f";
    private static final String DOCUSIGN_USER_ID_SIRENKO = "71c9d1b0-dd4a-41a6-be9b-ca2dd9893e60";
    private static final int DOCUSIGN_JWT_EXPIRES_IN_SECONDS = 3600; // 1 hour
    private static final List<String> SCOPES;

    static {
        SCOPES = Arrays.asList("signature", "impersonation");
    }

    private static DocuSignApiClient instance;
    private final ApiClient apiClient;
    private OAuth.OAuthToken token;
    private Long expirationTimeMillis;

    public static DocuSignApiClient getInstance() throws Exception {
        if (Objects.nonNull(instance))
            return instance;

        synchronized (DocuSignApiClient.class) {
            if (Objects.isNull(instance))
                instance = new DocuSignApiClient();

            return instance;
        }
    }

    private DocuSignApiClient() throws Exception {
        this.apiClient = new ApiClient(Config.getBaseUrl());
        this.token = generateOAuthToken();
        System.out.println("Token: " + token);
        this.enrichClientWithToken();
    }

    private void enrichClientWithToken() {
        apiClient.setAccessToken(token.getAccessToken(), token.getExpiresIn());
        expirationTimeMillis = System.currentTimeMillis() + token.getExpiresIn() * MILLIS_PER_SECOND;
    }

    public String getAccessToken() throws Exception {
        if (isTokenAlive())
            return token.getAccessToken();

        refreshToken();
        return token.getAccessToken();
    }

    public List<OAuth.Account> getAllAccounts() throws Exception {
        ApiClient client = getApiClient();
        return client.getUserInfo(client.getAccessToken()).getAccounts();
    }

    public ApiClient getApiClient() throws Exception {
        if (isTokenAlive())
            return apiClient;

        refreshToken();
        return apiClient;
    }

    private boolean isTokenAlive() {
        return System.currentTimeMillis() < expirationTimeMillis - 15 * 60 * MILLIS_PER_SECOND;
    }

    private synchronized void refreshToken() throws Exception {
        if (!isTokenAlive()) {
            this.token = generateOAuthToken();
            enrichClientWithToken();
        }
    }

    private OAuth.OAuthToken generateOAuthToken() throws Exception {
        try {
            File privateKeyFile = new File(ClassLoader.getSystemResource(Config.getDocusignRsaPrivateKeyPath()).getFile());
            byte[] rsaPrivateKey = FileUtils.readFileToByteArray(privateKeyFile);
            return this.apiClient.requestJWTUserToken(
                    Config.getDocusignIntegrationKey(),
                    DOCUSIGN_USER_ID_HORDIEIKO,
                    SCOPES,
                    rsaPrivateKey,
                    DOCUSIGN_JWT_EXPIRES_IN_SECONDS);
        } catch (Exception e) {
            throw new Exception("Can not generate Access Token!", e);
        }
    }
}
