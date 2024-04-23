package edu.san;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class ExampleResource {

    private Map<String, Double> products = new HashMap<>();
    private Map<String, Integer> cart = new HashMap<>();

    public ExampleResource() {
        // Inicjalizacja produktów
        products.put("Mleko", 3.90);
        products.put("Woda niegazowana", 1.80);
        products.put("Coca-cola", 5.50);
        products.put("Szynka", 12.50);
        products.put("Jabłko", 2.00);
        products.put("Herbata", 4.20);
        products.put("Kabanosy", 8.90);
        products.put("Paluszki", 2.00);
    }


    @GET
    @Path("/products")
    @Produces(MediaType.TEXT_HTML)
    public String getShopPage() throws IOException {
        String productsHtmlContent = new String(Files.readAllBytes(Paths.get("src/main/resources/META-INF/resources/products.html")));
        return productsHtmlContent;
    }

    @POST
    @Path("/shop")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addToCart(@FormParam("product") String product, @FormParam("quantity") int quantity) {
        cart.put(product, quantity);
    }

    @GET
    @Path("/orders")
    @Produces(MediaType.TEXT_HTML)
    public String getOrderDetails() throws IOException {
        StringBuilder orderDetails = new StringBuilder();
        double totalPrice = 0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            double price = products.get(productName);
            double subtotal = price * quantity;
            orderDetails.append("<div class=\"order-item\">")
                    .append(productName).append(": ")
                    .append(quantity).append(" x ")
                    .append(price).append(" zł = ")
                    .append(subtotal).append(" zł")
                    .append("</div>");
            totalPrice += subtotal;
        }

        String ordersHtmlContent = new String(Files.readAllBytes(Paths.get("src/main/resources/META-INF/resources/orders.html")));

        String orderHtml = ordersHtmlContent.replace("{orderDetails}", orderDetails.toString());
        orderHtml = orderHtml.replace("{totalPrice}", String.valueOf(totalPrice));

        // Wstaw listę produktów i łączną cenę zamówienia do orders.html
        return orderHtml;
    }
}

