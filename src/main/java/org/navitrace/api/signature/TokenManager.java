
package org.navitrace.api.signature;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.navitrace.storage.StorageException;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Singleton
public class TokenManager {

    private static final int DEFAULT_EXPIRATION_DAYS = 7;

    private final ObjectMapper objectMapper;
    private final CryptoManager cryptoManager;

    public static class TokenData {
        @JsonProperty("u")
        private long userId;
        @JsonProperty("e")
        private Date expiration;

        public long getUserId() {
            return userId;
        }

        public Date getExpiration() {
            return expiration;
        }
    }

    @Inject
    public TokenManager(ObjectMapper objectMapper, CryptoManager cryptoManager) {
        this.objectMapper = objectMapper;
        this.cryptoManager = cryptoManager;
    }

    public String generateToken(long userId) throws IOException, GeneralSecurityException, StorageException {
        return generateToken(userId, null);
    }

    public String generateToken(
            long userId, Date expiration) throws IOException, GeneralSecurityException, StorageException {
        TokenData data = new TokenData();
        data.userId = userId;
        if (expiration != null) {
            data.expiration = expiration;
        } else {
            data.expiration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(DEFAULT_EXPIRATION_DAYS));
        }
        byte[] encoded = objectMapper.writeValueAsBytes(data);
        return Base64.encodeBase64URLSafeString(cryptoManager.sign(encoded));
    }

    public TokenData verifyToken(String token) throws IOException, GeneralSecurityException, StorageException {
        byte[] encoded = cryptoManager.verify(Base64.decodeBase64(token));
        TokenData data = objectMapper.readValue(encoded, TokenData.class);
        if (data.expiration.before(new Date())) {
            throw new SecurityException("Token has expired");
        }
        return data;
    }

}
