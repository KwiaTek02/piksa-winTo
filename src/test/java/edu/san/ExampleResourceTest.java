package edu.san;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class ExampleResourceTest {


    @Test
    public void testGetShopPage() {
        given()
                .when().get("/products")
                .then()
                .statusCode(200)
                .body(containsString("Simple Online Shop"));
    }

    @Test
    public void testAddToCart() {
        given()
                .param("product", "Mleko")
                .param("quantity", 2)
                .when().post("/shop")
                .then()
                .statusCode(204);

        // Add more tests to check other scenarios
    }

    @Test
    public void testGetOrderDetails() {
        // Add products to the cart
        given()
                .param("product", "Mleko")
                .param("quantity", 2)
                .when().post("/shop");

        // Check if order details contain Milk with the correct quantity
        given()
                .when().get("/orders")
                .then()
                .statusCode(200)
                .body(containsString("Mleko: 2 x"))
                .body(containsString("Cena łączna"));
    }
}
