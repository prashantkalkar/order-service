package com.ee.orderservice;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.ee.orderservice.OrderServiceController.OrderDetails;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
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

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class OrderServiceTest {

    public static final String USER_ID = "pareshId";

    @Autowired
    private TestRestTemplate restTemplate;

    @Rule
    public PactProviderRuleMk2 mockDepartmentProvider = new PactProviderRuleMk2("user_service", "localhost", 8888, this);

    /**
     * Setup the user service mock server. Method name can be anything but must be annotated with @pact
     */
    @Pact(provider = "user_service", consumer = "order_service")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder.given("User Paresh exists")
                .uponReceiving("user GET request")
                .path("/user/" + USER_ID)
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(newJsonBody((a) -> {
                    a.stringValue("name", "paresh");
                    a.stringValue("email", "paresh@ee.com");
                    a.stringValue("address", "pune");
                }).build()).toPact();
    }


    @Test
    @PactVerification("user_service")
    public void springWiringTest() {
        // Given
        OrderDetails orderDetails = new OrderDetails("productId", "1", USER_ID);

        // When
        OrderResponse orderResponse = restTemplate.postForObject("/order", getHttpRequest(orderDetails), OrderResponse.class);

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

    @Before
    public void setup() {
        restTemplate.getRestTemplate().getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @NotNull
    private HttpEntity<OrderDetails> getHttpRequest(OrderDetails orderDetails) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(orderDetails, httpHeaders);
    }
}
