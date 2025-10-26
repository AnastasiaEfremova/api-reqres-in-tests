package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import config.ReqresConfigProvider;

import static io.restassured.http.ContentType.JSON;
import static helpers.CustomAllureListener.withCustomTemplates;

public class ApiSpecs {

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .addFilter(withCustomTemplates())
                .setBaseUri(ReqresConfigProvider.config.baseUrl())
                .addHeader("x-api-key", ReqresConfigProvider.config.apiKey())
                .setContentType(JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification getSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification getCreatedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification getNoContentResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(204)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification getBadRequestResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification getAuthSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .setBasePath("/login")
                .build();
    }

    public static RequestSpecification getUsersSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .setBasePath("/users")
                .build();
    }

    public static RequestSpecification getRegisterSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .setBasePath("/register")
                .build();
    }
}