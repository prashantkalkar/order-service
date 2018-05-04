package com.ee.orderservice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderServiceController {

    @PostMapping(path = "/order", consumes = "application/json", produces = "application/json")
    public String placeOrder(@RequestBody OrderDetails orderDetails) {
        RestTemplate restTemplate = new RestTemplate();
        User userDetails = restTemplate.getForObject("http://localhost:8888/user/" + orderDetails.getUserId(), User.class);
        System.out.println("User details obtained: " + userDetails);

        return "{\"orderId\" : \"7998289398179\"}";
    }

    public static class OrderDetails {
        private String productId;
        private String quantity;
        private String userId;

        public OrderDetails() {
            // default constructor for json conversion.
        }

        public OrderDetails(String productId, String quantity, String userId) {
            this.productId = productId;
            this.quantity = quantity;
            this.userId = userId;
        }

        public String getProductId() {
            return productId;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getUserId() {
            return userId;
        }

        @Override
        public String toString() {
            return "OrderDetails{" +
                    "productId='" + productId + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }


    public static class User {
        private String name;
        private String email;
        private String address;


        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
