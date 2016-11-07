package sod.api

import com.google.common.net.MediaType
import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import sod.BaseSodSpecification
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by cesaregb on 9/24/16.
 */

class ClientSpec extends BaseSodSpecification {

    def clientDto = null;

    void setup() {
        println("User: " + rUser);
    }

    @Shared
    String rUser = "test-"+RandomStringUtils.randomAlphanumeric(10)+"@tersuslavanderia.com".toLowerCase();

    def "Save/Update Client"() {
        when: "Call save/update client. "
        def body = [
                email         : rUser ,
                lastName      : "Gonzalez",
                name          : "Pedro",
                password      : "___",
                twitter       : "algo",
                loginID       : "",
                rfc           : "",
                razonSocial   : "",
                defaultPhone  : "string",
                defaultAddress: "string",
                defaultPayment: "string",
                idClientType  : 3
        ];
        def response = restClient.put(path: "clients", body: body, requestContentType: MediaType.JSON_UTF_8)
        clientDto = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then: "The correct status is expected. "
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
            data.idClient > 0
        }

    }

    def "Save/Delete/Reactivate Client"() {
        when: "Call save/update client. "
        def body = [
                email         : rUser,
                lastName      : "Gonzalez",
                name          : "Pedro",
                password      : "___",
                twitter       : "algo",
                loginID       : "",
                rfc           : "",
                razonSocial   : "",
                defaultPhone  : "string",
                defaultAddress: "string",
                defaultPayment: "string",
                idClientType  : 3
        ];
        def response = restClient.put(path: "clients", body: body, requestContentType: MediaType.JSON_UTF_8)
        clientDto = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then: "The correct status is expected. "
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
            data.idClient > 0
        }

        when:
        response = restClient.delete(path: "clients/" + clientDto.idClient, requestContentType: MediaType.JSON_UTF_8)
        println "Delete client: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK
        }

        when:
        response = restClient.put(path: "clients/reactivate/" + body.email, requestContentType: MediaType.JSON_UTF_8)
        println "Reactivate client: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK
        }

    }

    def "Test add Client to Client Type"() {
        given:
            def sodClient = clientDto
            println "Given section : " + new JsonBuilder(sodClient).toPrettyString()
        when: "Call save client. "
            // get client
            sodClient = restClient.get(path: "clients", query: ['email' : rUser]).data
            println "Initial" + new JsonBuilder(sodClient).toPrettyString()

            // update client type
            restClient.post(path: "clients/clientType/addClients/2", body: sodClient, requestContentType: MediaType.JSON_UTF_8)

            // get client again..
            sodClient = restClient.get(path: "clients", query: ['email' : rUser])
            println "updated" + new JsonBuilder(sodClient.data).toPrettyString()

        then: "The correct status is expected. "
            with(sodClient) {
                status == HttpStatus.SC_OK
                data[0].idClientType == 2
            }

    }

    def "Get Client"() {
        when: "Call save client. "
        String uri = uris['clients']
        def sodClient = restClient.get(path: "clients", query: ['email' : rUser])

        then: "The correct status is expected. "
        with(sodClient) {
            status == HttpStatus.SC_OK
            println "Initial " + new JsonBuilder(data).toPrettyString()
            data.idClientType == [2]
        }
    }

    def "Test Client properties."() {
        given:
        String uri = uris['clients']
        def sodClient = restClient.get(path: "clients", query: ['email' : rUser]).data;
        def idClient = sodClient.idClient[0];

        when:
        def body = [
                "address": "address",
                "address2": "address2",
                "city": "Zacatecas",
                "country": "Mexico",
                "zipcode": "98054",
                "state": "Zacatecas",
                "lat": 0,
                "lng": 0,
                "comments": "string",
                "idClient": sodClient.idClient[0],
                "prefered": true
        ];
        def response = restClient.post(path: "clients/address", body: body, requestContentType: MediaType.JSON_UTF_8)
        println "Address: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
            data.idClient > 0
        }

        when:
        body = [
                "inOrder": false,
                "number": "1",
                "idBagtype": 1,
                "idClient": sodClient.idClient[0]
        ];
        response = restClient.post(path: "clients/client-bag", body: body, requestContentType: MediaType.JSON_UTF_8)
        println "Bag: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
            data.idClient > 0
        }

        when:
        def bagInfo = response.data;
        def idClientBag = bagInfo.idClientBags;
        response = restClient.delete(path: "clients/client-bag/" + idClientBag, requestContentType: MediaType.JSON_UTF_8)
        println "Delete bag response: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK
        }

        when:
        body = [
                "number": "00101001",
                "prefered": false,
                "idClient": sodClient.idClient[0]
        ];
        response = restClient.post(path: "clients/phone-number", body: body, requestContentType: MediaType.JSON_UTF_8)
        println "Phone number: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
            data.idClient > 0
        }

        when:
        body = [
                "token": "string",
                "type": 0,
                "prefered": false,
                "idClient": sodClient.idClient[0]
        ];
        response = restClient.post(path: "clients/client-payment-info", body: body, requestContentType: MediaType.JSON_UTF_8)
        println "Client payment: " + new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
            data.idClient > 0
        }

        when:
        response = restClient.delete(path: "clients/" + idClient, requestContentType: MediaType.JSON_UTF_8)
        println "Delete client: " + new JsonBuilder(response.data).toPrettyString()
        then:
        with(response) {
            status == HttpStatus.SC_OK
        }
    }


}