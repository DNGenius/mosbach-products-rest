package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Token;


public class TokenImpl implements Token {

    private String tokenValue;
    private String userID;

    public TokenImpl (String tokenValue, String userID) {
        this.tokenValue = tokenValue;
        this.userID = userID;
    }

    @Override
    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
