package com.udla.contracts;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.consumer.junit5.PactVerificationInvocationContextProvider;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PactTestFor(providerName = "payment-provider")
public class PaymentConsumerPactTest {

    @Pact(consumer = "payment-consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .uponReceiving("Authorize payment")
            .path("/payments/authorize")
            .method("POST")
            .headers(Map.of("Content-Type", "application/json"))
            .body("{\"amount\":100,\"currency\":\"USD\"}")
            .willRespondWith()
            .status(200)
            .headers(Map.of("Content-Type", "application/json"))
            .body("{\"status\":\"AUTHORIZED\"}")
            .toPact();
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testPaymentAuthorization(MockServer mockServer) throws Exception {
        String url = mockServer.getUrl() + "/payments/authorize";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"amount\":100,\"currency\":\"USD\"}"))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("{\"status\":\"AUTHORIZED\"}", response.body());
    }
}
