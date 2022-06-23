package adquirencia.interactions;

import adquirencia.exceptions.ErrorServicesException;
import adquirencia.models.TestData;
import io.restassured.RestAssured;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Get;
import org.apache.http.HttpStatus;

import static adquirencia.utils.Constant.Constants.EXCEPTION_ERROR_CONSUMPTION_SERVICE;
import static io.restassured.http.ContentType.JSON;

public class ExecuteGetkey implements Interaction {

    private final String resource;

    public ExecuteGetkey(String resource) {
        this.resource = resource;
    }

    public static ExecuteGetkey service(String resource) {
        return Tasks.instrumented(ExecuteGetkey.class, resource);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        SerenityRest.reset();
        RestAssured.proxy("piscis01.bancodebogota.net",8003);
        actor.attemptsTo(
                Get.resource(resource)
                        .with(request -> request.
                                contentType(JSON).params(TestData.getData())
                                .relaxedHTTPSValidation().headers("X-CustIdentType","CC")
                                .headers("X-CustIdentNum","52805999").log().all()
                        )
        );

        if(SerenityRest.lastResponse().statusCode() != HttpStatus.SC_OK){
            throw new ErrorServicesException(EXCEPTION_ERROR_CONSUMPTION_SERVICE);
        }

    }
}
