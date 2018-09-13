package com.wiremock.test;

import static org.hamcrest.Matchers.equalTo;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.jayway.restassured.RestAssured.given;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class WireMockResponseBasedOnRequestTest {

    private static final String MAPPING_JSON = "{\"request\":{\"method\":\"GET\",\"urlPattern\":\"/ram/kumar/.*\"},\"response\":{\"status\":200,\"body\":\"Default Text\",\"transformers\":[\"bodyContentTransformer\"]}}";

    private WireMockServer server;

    @Before
    public void before() {
        server = new WireMockServer(wireMockConfig().extensions(BodyContentTransformer.class));
        StubMapping stubMapping = StubMapping.buildFrom(MAPPING_JSON);
        server.addStubMapping(stubMapping);
        server.start();
    }

    @After
    public void after() {
        server.stop();
    }

    @Test
    public void testWithValidEndPoint() {

        given()
            .when()
            .get("http://localhost:8080/ram/kumar/10")
            .then()
            .statusCode(200)
            .body(equalTo("10"));

        server.stop();

    }

    @Test
    public void testWithInValidEndPoint() {
        given()
            .when()
            .get("http://localhost:8080/invalid/path")
            .then()
            .statusCode(404);
    }


}
