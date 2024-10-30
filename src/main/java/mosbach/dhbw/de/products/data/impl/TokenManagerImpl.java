package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Token;
import mosbach.dhbw.de.products.data.api.TokenManager;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TokenManagerImpl implements TokenManager {

    private static TokenManagerImpl tokenManagerImpl = null;
    String fileName = "tokens.properties";

    private TokenManagerImpl() { }

    public static TokenManagerImpl getTokenManagerImpl() {
        if (tokenManagerImpl == null) {
            tokenManagerImpl = new TokenManagerImpl();
        }
        return tokenManagerImpl;
    }

    @Override
    public Token generateToken(String userID) {
        String tokenValue = UUID.randomUUID().toString();
        Token newToken = new TokenImpl(tokenValue, userID);
        List<Token> currentTokens = getAllTokens();
        Logger logger = Logger.getLogger("TokenManager");
        logger.info("Anzahl der Tokens vor dem Entfernen: " + currentTokens.size());
        logger.info("Zu entfernende userID: " + userID);
        boolean removed = currentTokens.removeIf(token -> token.getUserID().equals(userID)); // Entfernt alten Token, falls vorhanden
        logger.info("Anzahl der Tokens nach dem Entfernen: " + currentTokens.size());
        logger.info("Wurde ein Token entfernt? " + removed);
        currentTokens.add(newToken);
        Logger
                .getLogger("TokenManager")
                .log(Level.INFO, "Token hinzugefügt mit: " + tokenValue + userID);
        setAllTokens(currentTokens);
        return newToken;
    }

    @Override
    public List<Token> getAllTokens() {
        Properties properties = new Properties();
        List<Token> tokens = new ArrayList<>();

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }
            Logger
                    .getLogger("TokenManager")
                    .log(Level.INFO, "File content: " + properties.toString());

            for (String userID : properties.stringPropertyNames()) {
                String tokenValue = properties.getProperty(userID);
                Logger
                        .getLogger("TokenManager")
                        .log(Level.INFO, "Loaded token: " + tokenValue + " for userID: " + userID);
                tokens.add(new TokenImpl(tokenValue, userID));
            }

            Logger.getLogger("TokenManager").log(Level.INFO, "Tokens loaded successfully");
        } catch (IOException e) {
            Logger.getLogger("TokenManager").log(Level.WARNING, "Error loading tokens file", e);
        }

        return tokens;
    }

    @Override
    public void setAllTokens(List<Token> tokens) {
        Properties properties = new Properties();

        for (Token token : tokens) {
            properties.setProperty(token.getUserID(), token.getTokenValue());
        }

        try (OutputStream output = new FileOutputStream(fileName)) {
            properties.store(output, null);
            Logger.getLogger("TokenManager").log(Level.INFO, "Tokens saved successfully");
        } catch (IOException e) {
            Logger.getLogger("TokenManager").log(Level.WARNING, "Error saving tokens file", e);
        }
    }

    public boolean validateToken(String tokenValue) {
        List<Token> tokens = getAllTokens();
        return tokens.stream().anyMatch(token -> token.getTokenValue().equals(tokenValue));
    }

    public void removeToken(String userID) {
        List<Token> tokens = getAllTokens();
        Logger
                .getLogger("TokenManager")
                .log(Level.INFO, "Tokens before removeToken: " + tokens);
        tokens.removeIf(token -> token.getUserID().equals(userID));
        Logger
                .getLogger("TokenManager")
                .log(Level.INFO, "Tokens after removeToken: " + tokens);
        setAllTokens(tokens);
    }

    public String getUserIDFromToken(String tokenValue) {
        List<Token> tokens = getAllTokens();
        Logger
                .getLogger("TokenManager")
                .log(Level.INFO, "Übergebener token: " + tokenValue);
        for (Token token : tokens) {
            Logger
                    .getLogger("TokenManager")
                    .log(Level.INFO, "Checking token: " + token.getTokenValue());
            if (token.getTokenValue().equals(tokenValue)) {
                Logger
                        .getLogger("TokenManager")
                        .log(Level.INFO, "Found matching token for userID: " + token.getUserID());
                return token.getUserID();
            }
        }
        Logger
                .getLogger("TokenManager")
                .log(Level.INFO, "No matching token found");
        return null;
    }

}
