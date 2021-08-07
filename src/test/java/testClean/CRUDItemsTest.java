
package testClean;

import factoryRequest.FactoryRequest;
import factoryRequest.RequestInformation;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import util.ConfigAPI;
import util.GetProperties;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;


public class CRUDItemsTest {

    private String projectId ="";

    @Before
    public void verifyCreateforProject()  throws IOException {
        new GetProperties().leerPropiedades();
        JSONObject body = new JSONObject();
        body.put("Content","JAQC_Project");
        body.put("Icon",12);

        RequestInformation request = new RequestInformation(ConfigAPI.CREATE_PROJECT,body.toString());
        Response response = FactoryRequest.make(FactoryRequest.POST).send(request);
        response.then()
                .statusCode(200)
                .body("Content", equalTo("JAQC_Project"));
        projectId = response.then().extract().path("Id")+"";
    }

    @Test
    public void verifyCRUDforItem(){
        JSONObject body = new JSONObject();
        body.put("Content","JAQC_item");
        body.put("ProjectId", projectId);

        //Crear item en proyecto especifico
        RequestInformation request = new RequestInformation(ConfigAPI.CREATE_ITEMS,body.toString());
        Response response = FactoryRequest.make(FactoryRequest.POST).send(request);
        response.then()
                .statusCode(200)
                .body("Content", equalTo("JAQC_item"));
        String id = response.then().extract().path("Id")+"";

        //Actualizar item creado
        body.put("Content","JAQC_item_update");
        request = new RequestInformation(ConfigAPI.UPDATE_ITEMS.replace("ID",id),body.toString());
        response = FactoryRequest.make(FactoryRequest.PUT).send(request);
        response.then()
                .statusCode(200)
                .body("Content", equalTo("JAQC_item_update"));

        //Obtener item creado
        request = new RequestInformation(ConfigAPI.READ_ITEMS.replace("ID",id),"");
        response = FactoryRequest.make(FactoryRequest.GET).send(request);
        response.then()
                .statusCode(200)
                .body("Content", equalTo("JAQC_item_update"));

        //Eliminar item creado
        request = new RequestInformation(ConfigAPI.DELETE_ITEMS.replace("ID",id),"");
        response = FactoryRequest.make(FactoryRequest.DELETE).send(request);
        response.then()
                .statusCode(200)
                .body("Deleted", equalTo(true));

    }

}

