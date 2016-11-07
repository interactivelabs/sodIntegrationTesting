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
class SpecSpec extends BaseSodSpecification{
    @Shared
    String specPath = "specs";

    @Shared
    String specValuePath = "specs/specs-values";

    @Shared
    String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

    void setup() {
        println("specValuePath: "+specValuePath+" \n nameHelper: " + nameHelper);
    }

    def "Specs test"() {
        when: "Create 1st spec"
        def body = [
                "description": "string",
                "name": nameHelper,
                "optional": 0,
                "max_qty": 100,
                "primarySpec": true,
                "deleted": 0
        ];
        def response = restClient.post(path: specPath, body: body, requestContentType: MediaType.JSON_UTF_8)
        def type1 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create 2nd type"
        body = [
                "description": "string",
                "name": "2-" + nameHelper,
                "optional": 0,
                "max_qty": 100,
                "primarySpec": false,
                "deleted": 0
        ];
        response = restClient.post(path: specPath, body: body, requestContentType: MediaType.JSON_UTF_8)
        def type2 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create spec 1"
        body = [
                "idSupplyType": 0,
                "type": 1,
                "value": "Value1",
                "idSpecs": type1.idSpecs,
                "serviceIncrement": 0,
                "prefered": 0,
                "specPrice": 20,
                "costType": 1
        ];
        response = restClient.post(path: specValuePath, body: body, requestContentType: MediaType.JSON_UTF_8)
        def child = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create spec 2"
        body = [
		        "idSupplyType": 1,
		        "type": 1,
		        "value": "",
		        "idSpecs": type2.idSpecs,
		        "serviceIncrement": 15,
		        "prefered": 0,
		        "specPrice": 0,
		        "costType": 2
        ];
        response = restClient.post(path: specValuePath, body: body, requestContentType: MediaType.JSON_UTF_8)
        def child2 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

	    when: "Update Spec 1"
	    body = child;
	    body.idSpecs = type2.idSpecs;
	    response = restClient.put(path: specValuePath, body: body, requestContentType: MediaType.JSON_UTF_8)
	    println new JsonBuilder(response.data).toPrettyString()

	    then:
	    with(response) {
		    status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
	    }

        when: "Delete Parents"
        restClient.delete(path: new StringBuilder(specPath).append("/").append(+type1.idSpecs), requestContentType: MediaType.JSON_UTF_8)
        response = restClient.delete(path: new StringBuilder(specPath).append("/").append(+type2.idSpecs), requestContentType: MediaType.JSON_UTF_8)
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }
    }

}
