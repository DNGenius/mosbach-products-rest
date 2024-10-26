package mosbach.dhbw.de.products.data.api;

import java.util.List;

public interface TokenManager {

    Token generateToken(String userId);
    boolean validateToken(String tokenValue);
    void removeToken(String userId);
    List<Token> getAllTokens();
    void setAllTokens(List<Token> tokens);
    String getUserIDFromToken(String tokenValue);

}
