package sod.api

import com.google.common.net.MediaType
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import sod.BaseSodSpecification
import spock.lang.Shared

/**
 * Created by cesaregb on 11/6/16.
 */
class ProductSpec extends BaseSodSpecification{
	@Shared
	def paths = [productType: "products/product-type",
	             products: "products"];

	@Shared
	String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

	@Shared
	def jsonSlurper;

    void setup() {
	    jsonSlurper = new JsonSlurper()
        println("path: "+paths+" \n nameHelper: " + nameHelper);
    }

    def "Crud product type "() {
        when:"Create"
        def body = [
		        "deleted": 0,
		        "description": "string",
		        "name": nameHelper
        ];
        def response = restClient.post(path: paths["productType"], body: body, requestContentType: MediaType.JSON_UTF_8)
        def responseBody = response.data;
        println new JsonBuilder(responseBody).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when:"Update"
        body = [
		        "idProductType": responseBody.idProductType,
		        "deleted": 0,
		        "description": "description",
		        "name": nameHelper
        ];
        response = restClient.put(path: paths["productType"], body: body, requestContentType: MediaType.JSON_UTF_8)
        responseBody = response.data;
        println new JsonBuilder(responseBody).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK
        }

        when: "Delete Parents"
        response = restClient.delete(path: new StringBuilder(paths["productType"]).append("/").append(+responseBody.idProductType), requestContentType: MediaType.JSON_UTF_8)
        println new JsonBuilder(response.data).toPrettyString()
        then:
        with(response) {
            status == HttpStatus.SC_OK
        }
    }

	def "Crud product"() {
		given:
		def response = restClient.get(path: paths["productType"], requestContentType: MediaType.JSON_UTF_8);
		def productTypes = response.data;
		println ("-->" + productTypes[0]["idProductType"])

		when:"Create"
		def body = [
				"maxQty": 100,
				"idProductType": productTypes[0]["idProductType"],
				"name": nameHelper,
				"price": 130

		];
		response = restClient.post(path: paths["products"], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}

		when:"Update"
		body = [
				"idProduct": responseBody.idProduct,
				"maxQty": 120,
				"idProductType": productTypes[0]["idProductType"],
				"name": nameHelper,
				"price": 300
		];
		response = restClient.put(path: paths["products"], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when: "Delete Parents"
		response = restClient.delete(path: new StringBuilder(paths["products"]).append("/").append(+responseBody.idProduct), requestContentType: MediaType.JSON_UTF_8)
		println new JsonBuilder(response.data).toPrettyString()
		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}
}
