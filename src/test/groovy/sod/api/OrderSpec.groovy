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
class OrderSpec extends BaseSodSpecification{
    @Shared
    def paths = [orderType: "orders/order-type",
                 orderTypeTask: "orders/order-type/order-type-task"];

    @Shared
    String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

    void setup() {
        println("path: " + paths + " \n nameHelper: " + nameHelper);
    }
	

	def "Test CRUD Order Type"() {
		when:
		def body = [
				"description": "string",
				"name": nameHelper,
				"transportInfo": 3
		];
		def response = restClient.post(path: paths['orderType'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}


		when:
		body = [
				"idOrderType": responseBody.idOrderType,
				"description": "string 22222",
				"name": nameHelper,
				"transportInfo": 3
		];
		response = restClient.put(path: paths['orderType'], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['orderType'] + "/" + responseBody.idOrderType, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

	def "Test CRUD Order Type Task"() {
		given:
		def response = restClient.get(path: paths["orderType"], requestContentType: MediaType.JSON_UTF_8);
		def pResponse = response.data;
		def idParent = pResponse[0]["idOrderType"]
		println ("-->" + idParent)

		when:"Create"
		def body = [
				"idOrderType": idParent,
				"task": [ "idTask": 1 ],
				"sortingOrder": 1,
				"time": 30
		];
		response = restClient.post(path: paths['orderTypeTask'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}

		when:
		body = [
				"idOrderTypeTask": responseBody.idOrderTypeTask,
				"idOrderType": idParent,
				"task":["idTask": 1],
				"sortingOrder": 1,
				"time": 40,
		];
		response = restClient.put(path: paths['orderTypeTask'], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['orderTypeTask'] + "/" + responseBody.idOrderTypeTask, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

}
