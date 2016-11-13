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
class PromotionSpec extends BaseSodSpecification{
    @Shared
    def paths = [promotionType: "promotions/promotion-type",
                 promotion: "promotions"];

    @Shared
    String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

    void setup() {
        println("path: " + paths + " \n nameHelper: " + nameHelper);
    }

	def "Test CRUD Promotion Type"() {
		when:
		def body = [
				"description": "string",
				"name": nameHelper
		];
		def response = restClient.post(path: paths['promotionType'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}


		when:
		body = [
				"deleted": 0,
				"idPromotionType": responseBody.idPromotionType,
				"description": "abcd",
				"name": nameHelper
		];
		response = restClient.put(path: paths['promotionType'], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['promotionType'] + "/" + responseBody.idPromotionType, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

	def "Test CRUD Promotion"() {
		given:
		def response = restClient.get(path: paths["promotionType"], requestContentType: MediaType.JSON_UTF_8);
		def pResponse = response.data;
		println (new JsonBuilder(pResponse).toPrettyString())
		def idParent = pResponse[0]["idPromotionType"]

		when:"Create"
		def body = [
				"idPromotionType": idParent,
				"name": nameHelper,
				"amount": 10,
				"description": "string",
				"maxUses": 1,
				"minimumAmount": 0,
				"orderLimit": 1,
				"promoCode": "string",
				"discountType": 2
		];

		response = restClient.post(path: paths['promotion'], body: body, requestContentType: MediaType.JSON_UTF_8)
		def responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
		}

		when:
		body = [
				"idPromotion": responseBody.idPromotion,
				"idPromotionType": idParent,
				"name": nameHelper,
				"amount": 20,
				"description": "string",
				"maxUses": 1,
				"minimumAmount": 0,
				"orderLimit": 1,
				"promoCode": "string",
				"discountType": 1
		];
		response = restClient.put(path: paths['promotion'], body: body, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}

		when:
		response = restClient.delete(path: paths['promotion'] + "/" + responseBody.idPromotion, requestContentType: MediaType.JSON_UTF_8)
		responseBody = response.data;
		println new JsonBuilder(responseBody).toPrettyString()

		then:
		with(response) {
			status == HttpStatus.SC_OK
		}
	}

}
