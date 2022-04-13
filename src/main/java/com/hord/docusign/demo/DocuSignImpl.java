package com.hord.docusign.demo;

import com.hord.docusign.DSTokenHandler;

import java.util.logging.Logger;

public class DocuSignImpl {

    private static final Logger logger = Logger.getLogger("MyLogger");

    // can be auto-detected upon ApiClient creation
    private static final String DOCUSIGN_AUTH_BASE_PATH = "account-d.docusign.com";
    public static final String DOCUSIGN_AUTH_NAME = "docusignAccessCode";

    public static void main(String[] args) throws Exception {
        String bearerToken = DSTokenHandler.getInstance().getBearerToken("337066", "vhordieiko@provectus.com");

        if (bearerToken != null)
            logger.info("Got Token!");

//        DocuSignApiClient dsClient = DocuSignApiClient.getInstance();

//        EnvelopesApi envelopesApi = new EnvelopesApi(dsClient.getApiClient());
//
//
//        for (OAuth.Account account : dsClient.getAllAccounts())
//            logger.info("Retrieved Account: " + account);
    }
}
