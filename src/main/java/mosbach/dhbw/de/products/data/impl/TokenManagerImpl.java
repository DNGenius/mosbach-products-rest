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
    String fileName =  "tokens.properties";

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
        currentTokens.removeIf(token -> token.getUserID().equals(userID)); // Entfernt alten Token, falls vorhanden
        currentTokens.add(newToken);
        setAllTokens(currentTokens);
        return newToken;
    }

    @Override
    public List<Token> getAllTokens() {
        Properties properties = new Properties();
        List<Token> tokens = new ArrayList<>();

        try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);

            for (String userID : properties.stringPropertyNames()) {
                String tokenValue = properties.getProperty(userID);
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
        tokens.removeIf(token -> token.getUserID().equals(userID));
        setAllTokens(tokens);
    }

    public String getUserIDFromToken(String tokenValue) {
        List<Token> tokens = getAllTokens();
        return tokens.stream()
                .filter(token -> token.getTokenValue().equals(tokenValue))
                .findFirst()
                .map(Token::getUserID)
                .orElse(null);
    }
}
