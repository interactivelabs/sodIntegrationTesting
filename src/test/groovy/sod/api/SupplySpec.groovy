package sod.api

import com.google.common.net.MediaType
import groovy.json.JsonBuilder
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import sod.BaseSodSpecification
import spock.lang.Shared

/**
 * Created by cesaregb on 11/6/16.
 */
class SupplySpec extends BaseSodSpecification{
    @Shared
    String path = "supplies";

    @Shared
    String pathType = "supplies/supply-type";

    @Shared
    String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

    void setup() {
        println("path: "+path+" \n nameHelper: " + nameHelper);
    }

    def "Add 2 Supplytype + Add Supply + change Supply from Supply type "() {
        when: "Create 1st type"
        def body = [
            "description": "description",
            "name": "type" + nameHelper
        ];
        def response = restClient.post(path: pathType, body: body, requestContentType: MediaType.JSON_UTF_8)
        def type1 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create 2nd type"
        body = [
            "description": "description2",
            "name": "type2" + nameHelper
        ];
        response = restClient.post(path: pathType, body: body, requestContentType: MediaType.JSON_UTF_8)
        def type2 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create Supply"
        body = [
                "description": "description",
                "name": nameHelper,
                "idSupplyType": type1.idSupplyType,
                "price": 20,
                "serviceIncrement": 15,
                "status": 0,
        ];
        response = restClient.post(path: path, body: body, requestContentType: MediaType.JSON_UTF_8)
        def child = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Update Supply"
        def updatePath = new StringBuilder(pathType).append("/").append(type2.idSupplyType).append("/child/").append(child.idSupply);
        response = restClient.put(path: updatePath, requestContentType: MediaType.JSON_UTF_8)
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

	    when: "Update Supply 2"
	    body = child;
	    body.idSupplyType = type1.idSupplyType;
	    response = restClient.put(path: path, body: body, requestContentType: MediaType.JSON_UTF_8)
	    println new JsonBuilder(response.data).toPrettyString()

	    then:
	    with(response) {
		    status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
	    }

        when: "Delete Parents"
        restClient.delete(path: new StringBuilder(pathType).append("/").append(+type1.idSupplyType), requestContentType: MediaType.JSON_UTF_8)
        response = restClient.delete(path: new StringBuilder(pathType).append("/").append(+type2.idSupplyType), requestContentType: MediaType.JSON_UTF_8)
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }
    }

}
