package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.Product;
import mosbach.dhbw.de.products.data.api.ProductManager;
import mosbach.dhbw.de.products.model.alexa.AlexaRO;
import mosbach.dhbw.de.products.model.alexa.OutputSpeechRO;
import mosbach.dhbw.de.products.model.alexa.ResponseRO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/alexa")
public class AlexaController {

    private final ProductManager productManager;

    @Autowired
    public AlexaController(ProductManager productManager) {
        this.productManager = productManager;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public AlexaRO readProducts(@RequestBody AlexaRO alexaRO) {

        String myAnswer = "";
        if (alexaRO.getRequest().getIntent().getName().equals("ProductsReadIntent")) {
            myAnswer += "Folgende Produkte werden im Wilde Wurst Waren Shop angeboten: ";
            List<Product> allProducts = productManager.getAllProducts();
            Collections.shuffle(allProducts);
            for (Product p : allProducts) {
                // Preis in Euro umwandeln und Formatierung anpassen
                String formattedPrice = String.format("%.2f", p.getPriceInEuro()).replace(".", ","); // z.B. 7,20
                // Aufteilen in Euro und Cent
                String[] priceParts = formattedPrice.split(",");
                String euroPart = priceParts[0]; // Euro-Teil
                String centPart = priceParts.length > 1 ? priceParts[1] : "00"; // Cent-Teil (Standard auf "00", falls nicht vorhanden)

                // Formatierung der Antwort
                myAnswer += p.getWeightInGrams() + " Gramm " + p.getDisplayName() + " für " + euroPart + " Euro";

                // Cent-Teil nur hinzufügen, wenn er nicht „00“ ist
                if (!centPart.equals("00")) {
                    myAnswer += " und " + centPart + " Cent";
                }

                myAnswer += ", "; // Trennzeichen für das nächste Produkt
            }
        }

        return
                prepareResponse(alexaRO, myAnswer, true);
    }


    private AlexaRO prepareResponse(AlexaRO alexaRO, String outText, boolean shouldEndSession) {

        alexaRO.setRequest(null);
        alexaRO.setContext(null);
        alexaRO.setSession(null);
        OutputSpeechRO outputSpeechRO = new OutputSpeechRO();
        outputSpeechRO.setType("PlainText");
        outputSpeechRO.setText(outText);
        ResponseRO response = new ResponseRO(outputSpeechRO, shouldEndSession);
        alexaRO.setResponse(response);
        return alexaRO;
    }
}
