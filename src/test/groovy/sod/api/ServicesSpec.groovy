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
class ServicesSpec extends BaseSodSpecification{
    @Shared
    def paths = [serviceCategory: "services/service-category",
                 serviceType: "services/service-type",
                 serviceTypeTask: "services/service-type/service-type-task",
                 productType: "products/product-type",
                 serviceTypeSpecs: "services/service-type/service-type-specs"];

    @Shared
    String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

    void setup() {
        println("path: " + paths + " \n nameHelper: " + nameHelper);
    }

    def "Test CRUD Service Category"() {
        when: "Create"
        def body = [
                "description": "description",
                "name": "cat-" + nameHelper,
        ];
        def response = restClient.post(path: paths['serviceCategory'], body: body, requestContentType: MediaType.JSON_UTF_8)
        def category = response.data;
        println new JsonBuilder(category).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

	    when:
	    body = [
			    "idServiceCategory": category.idServiceCategory,
			    "description": "description - 2",
			    "name": "cat-" + nameHelper,
	    ];
	    response = restClient.put(path: paths['serviceCategory'], body: body, requestContentType: MediaType.JSON_UTF_8)
	    category = response.data;
	    println new JsonBuilder(category).toPrettyString()

	    then:
	    with(response) {
		    status == HttpStatus.SC_OK
	    }


	    when:
	    response = restClient.delete(path: paths['serviceCategory'] + "/" + category.idServiceCategory, requestContentType: MediaType.JSON_UTF_8)
	    category = response.data;
	    println new JsonBuilder(category).toPrettyString()

	    then:
	    with(response) {
		    status == HttpStatus.SC_OK
	    }
    }

	def "Test CRUD Service Type"() {
		given:
		def response = restClient.get(path: paths["serviceCategory"], requestContentType: MediaType.JSON_UTF_8);
		def pResponse = response.data;
		int idParent = pResponse[0]["idServiceCategory"];

		response = restClient.get(path: paths["productType"], requestContentType: MediaType.JSON_UTF_8);
		pResponse = response.data;
		int idProductType = pResponse[0]["idProductType"];
		println ("-->" + idParent);

		when:
		def body = [
				"description": "string",
				"name": "type-" + nameHelper,
				"price": 100,
				"time": 40,
				"idServiceCategory": idParent,
				"calculator": false
		];
		response = restClient.post(path: paths['serviceType'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def type = response.data;
		println new JsonBuilder(type).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}

		when:
			def products = [[idProductType: idProductType]];
			response = restClient.post(path: paths['serviceType'] + "/addProducts/" + type.idServiceType, body: products, requestContentType: MediaType.JSON_UTF_8)
			println new JsonBuilder(response.data).toPrettyString()
		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		body = [
				"idServiceType": type.idServiceType,
				"description": "string",
				"name": "type-" + nameHelper,
				"price": 120,
				"time": 45,
				"idServiceCategory": 1,
				"calculator": false
		];
		response = restClient.put(path: paths['serviceType'], body: body, requestContentType: MediaType.JSON_UTF_8)
		type = response.data;
		println new JsonBuilder(type).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['serviceType'] + "/" + type.idServiceType, requestContentType: MediaType.JSON_UTF_8)
		type = response.data;
		println new JsonBuilder(type).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

	def "Test CRUD Service Type Spec"() {
		when:"Create"
		def body = [
				"idServiceType": 1,
				"spec": [ "idSpecs": 1 ]
		];
		def response = restClient.post(path: paths['serviceTypeSpecs'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}

		when:
		body = [
				"idServiceTypeSpecs": responseBody.idServiceTypeSpecs,
				"idServiceType": 1,
				"spec":["idSpecs": 2]
		];
		response = restClient.put(path: paths['serviceTypeSpecs'], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['serviceTypeSpecs'] + "/" + responseBody.idServiceTypeSpecs, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

	def "Test CRUD Service Type Task"() {
		when:"Create"
		def body = [
				"idServiceType": 1,
				"task": [ "idTask": 1 ],
				"sortingOrder": 1,
				"time": 30
		];
		def response = restClient.post(path: paths['serviceTypeTask'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}

		when:
		body = [
				"idServiceTypeTask": responseBody.idServiceTypeTask,
				"idServiceType": 1,
				"task":["idTask": 1],
				"sortingOrder": 1,
				"time": 40,
		];
		response = restClient.put(path: paths['serviceTypeTask'], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['serviceTypeTask'] + "/" + responseBody.idServiceTypeTask, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

}
