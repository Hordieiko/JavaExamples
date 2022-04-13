package com.hord.docusign;

public class Config {
    private static final String DOCUSIGN_BASE_PATH = "https://demo.docusign.net/restapi";
    private static final String DOCUSIGN_RSA_PRIVATE_KEY_PATH = "docusign/privateKey2.pem";
    private static final String DOCUSIGN_INTEGRATION_KEY = "532c2aad-b6f1-4f0a-a889-e7a3d387f0fb";
    private static final String DOCUSIGN_BASE_USER_ID = "3957ff3e-5518-4b7a-9f75-99364ce70ca4";

    public static String getBaseUrl() {
        return DOCUSIGN_BASE_PATH;
    }

    public static String getDocusignRsaPrivateKeyPath() {
        return DOCUSIGN_RSA_PRIVATE_KEY_PATH;
    }

    public static String getDocusignIntegrationKey() {
        return DOCUSIGN_INTEGRATION_KEY;
    }

    public static String getDocusignBaseUserId() {
        return DOCUSIGN_BASE_USER_ID;
    }
}
