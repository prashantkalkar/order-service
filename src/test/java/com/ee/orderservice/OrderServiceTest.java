package com.ee.orderservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class OrderServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void springWiringTest() {
        // Given
        OrderServiceController.OrderDetails orderDetails = new OrderServiceController.OrderDetails("productId", "1", "userId");
        restTemplate.getRestTemplate().getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<OrderServiceController.OrderDetails> httpEntity = new HttpEntity<>(orderDetails, httpHeaders);

        // When
        OrderResponse orderResponse = restTemplate.postForObject("/order", httpEntity, OrderResponse.class);

        // then
        assertThat(orderResponse.getOrderId(), not(isEmptyOrNullString()));
    }

    static class OrderResponse {
        private String orderId;

        public OrderResponse() {
            // default constructor.
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderId() {
            return orderId;
        }
    }
}
