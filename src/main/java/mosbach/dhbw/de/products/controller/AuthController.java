package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.CustomerManager;
import mosbach.dhbw.de.products.data.api.Token;
import mosbach.dhbw.de.products.data.api.TokenManager;
import mosbach.dhbw.de.products.model.LoginRequest;
import mosbach.dhbw.de.tasks.model.MessageAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenManager tokenManager;
    private final CustomerManager customerManager;

    @Autowired
    public AuthController(TokenManager tokenManager, CustomerManager customerManager) {
        this.tokenManager = tokenManager;
        this.customerManager = customerManager;
    }

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            String userEmail = loginRequest.getUserEmail();
            String userPassword = loginRequest.getUserPassword();

            if (customerManager.authenticateCustomer(userEmail, userPassword)) {
                // Erfolgreiche Authentifizierung
                Token token = tokenManager.generateToken(customerManager.getCustomerIDByEmail(userEmail));
                return ResponseEntity.ok(token.getTokenValue());
            } else {
                // Email oder Passwort falsch / nicht vorhanden
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            // Unerwarteter Fehler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {

        // Extrahiert tokenValue aus dem Authorization-Header und userID durch den Token
        if (token != null && token.startsWith("Bearer ")) {
            String tokenValue = token.substring(7);
            String userID = tokenManager.getUserIDFromToken(tokenValue);

            // Entfernt den Token, wenn gültige userID gefunden wird
            if (userID != null) {
                tokenManager.removeToken(userID);
                Logger
                        .getLogger("AuthController")
                        .log(Level.INFO, "User logged out: " + userID);
                return ResponseEntity.ok("Logged out successfully");
            }
        }
        // Fehler, falls kein gültiger Token oder userID
        Logger
                .getLogger("AuthController")
                .log(Level.INFO, "Logout failed: Invalid token");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid operation");
    }

}

