package com.olavgg.pc;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ApiTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() throws IOException {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
        new PasswordReader().read();
    }

    @AfterClass
    public static void stopServer() {
        if(server != null) {
            server.stop();
        }
        if(client != null) {
            client.stop();
        }
    }


    @Test
    public void testShasumOk(){
        HttpResponse response =
                client
                        .toBlocking()
                        .exchange(HttpRequest.GET("/sha1/7C4A8D09CA3762AF"));

        assertEquals(200, response.getStatus().getCode());
    }

    @Test
    public void testShasumInvalid(){
        try{
            client.toBlocking()
                    .exchange(HttpRequest.GET("/sha1/7C4Aaaaaa"));
        } catch (HttpClientResponseException responseException){
            assertEquals(400, responseException.getStatus().getCode());
        }
    }

    @Test
    public void testShasumWithLowercase(){
        String shasum = "f7c3bc1d808e04732adf679965ccc34ca7ae344";
        HttpResponse response =
            client
                .toBlocking()
                .exchange(HttpRequest.GET("/sha1/" + shasum));

        assertEquals(200, response.getStatus().getCode());
    }

    @Test
    public void testPasswordOk(){
        HttpResponse response =
                client
                        .toBlocking()
                        .exchange(HttpRequest.GET("/password/password"));

        assertEquals(200, response.getStatus().getCode());
    }

    @Test
    public void testPasswordNotFound(){
        try{
            client.toBlocking()
                    .exchange(HttpRequest.GET("/password/640oberon"));
        } catch (HttpClientResponseException responseException){
            assertEquals(404, responseException.getStatus().getCode());
        }
    }
}
