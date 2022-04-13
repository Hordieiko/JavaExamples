package com.hord.docusign;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.JWTUtils;
import com.docusign.esign.client.auth.OAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DSToken {

    private static final long DOCUSIGN_JWT_EXPIRES_IN_SECONDS = 3600L; // 1 hour
    private static final List<String> SCOPES = Arrays.asList("signature", "impersonation");

    private final ApiClient apiClient;
    private final String userId;

    private OAuth.OAuthToken oAuthToken;
    private LocalTime tokenExpirationTime;

    public DSToken(String userId) throws Exception {
        this.userId = userId;
        this.apiClient = new ApiClient(Config.getBaseUrl());
        this.generate();
    }

    public boolean isExpired() {
        return LocalTime.now().isAfter(tokenExpirationTime.minus(15, ChronoUnit.MINUTES));
    }

    public String getToken() {
        return oAuthToken.getAccessToken();
    }

    private synchronized void generate() throws ConsentRequiredException, Exception {
        try {
            File privateKeyFile = new File(ClassLoader.getSystemResource(Config.getDocusignRsaPrivateKeyPath()).getFile());
            byte[] rsaPrivateKey = FileUtils.readFileToByteArray(privateKeyFile);

            String jwtAssertion = getAssertion(rsaPrivateKey);
            System.out.println("JWT Assertion: " + jwtAssertion);

            oAuthToken = apiClient.requestJWTUserToken(
                    Config.getDocusignIntegrationKey(),
                    userId,
                    SCOPES,
                    rsaPrivateKey,
                    DOCUSIGN_JWT_EXPIRES_IN_SECONDS);

            tokenExpirationTime = LocalTime.now().plus(oAuthToken.getExpiresIn(), ChronoUnit.SECONDS);
        } catch (Exception e) {
            checkConsentRequiredException(e);
            throw new Exception("Can not generate Access Token!", e);
        }
    }

    private void checkConsentRequiredException(Exception e) throws ConsentRequiredException {
        if (e instanceof ApiException) {
            try {
                String responseBody = ((ApiException) e).getResponseBody();
                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                if ("consent_required".equals(jsonObject.get("error").getAsString()))
                    throw new ConsentRequiredException("Consent required from User with id: " + userId, e);
            } catch (ConsentRequiredException cr) {
                throw cr;
            } catch (Exception ignore) {
            }
        }
    }

    private static class ConsentRequiredException extends Exception {
        public ConsentRequiredException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public void refresh() throws ConsentRequiredException, Exception {
        generate();
    }

    private String getAssertion(byte[] rsaPrivateKey) throws Exception {
        RSAPrivateKey privateKey = getPrivateKey(rsaPrivateKey);
        Algorithm algorithm = Algorithm.RSA256(null, privateKey);
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 40));
        return JWT.create()
                .withIssuer("532c2aad-b6f1-4f0a-a889-e7a3d387f0fb") // clientId/integrationKey
                .withAudience("account-d.docusign.com")
                .withIssuedAt(toDate(start))
                .withClaim("scope", "signature impersonation")
                .withExpiresAt(toDate(start.plusHours(1)))
                .withSubject("3957ff3e-5518-4b7a-9f75-99364ce70ca4") // userId
                .sign(algorithm);
    }

    private static Date toDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    private RSAPrivateKey getPrivateKey(byte[] rsaPrivateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        PemReader reader = new PemReader(new StringReader(new String(rsaPrivateKey)));
        PemObject pemObject = reader.readPemObject();
        byte[] rsaPrivateKeyContent = pemObject.getContent();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(rsaPrivateKeyContent);
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);
        return (RSAPrivateKey) privateKey;
    }
}
