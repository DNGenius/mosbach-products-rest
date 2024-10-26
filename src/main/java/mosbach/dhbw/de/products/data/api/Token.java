package mosbach.dhbw.de.products.data.api;

public interface Token {
    String getTokenValue();
    String getUserID();

    // TODO expirationTime ?
}
