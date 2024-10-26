package mosbach.dhbw.de.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ProductsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsBackendApplication.class, args);
    }

}
