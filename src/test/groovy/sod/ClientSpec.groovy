package sod

import com.google.common.net.MediaType
import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient
import org.apache.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by cesaregb on 9/24/16.
 */

class ClientSpec extends Specification {

    int userNameTest = 0;
    def clientDto = null;

    void setup() {

//        uris = URIs.uris;
//        println("myProperty: " + Helper.getProperties())
        restClient = new RESTClient("http://localhost:8080/api/")
        restClient.handler.failure = restClient.handler.success
    }

    @Shared
    def restClient = null;

    @Shared
    def uris

    def "Save/Update Client"() {
        when: "Call save/update client. "
        println("Prefix: " + userNameTest)
        def body = [
                email         : "test@tersuslavanderia.com",
                lastName      : "string",
                name          : "string",
                password      : "string",
                twitter       : "string",
                loginID       : "string",
                rfc           : "string",
                razonSocial   : "string",
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

    def "Test add Client to Client Type"() {
        given:
            def sodClient = clientDto
            println "Given section : " + new JsonBuilder(sodClient).toPrettyString()
        when: "Call save client. "
            // get client
            sodClient = restClient.get(path: "clients", query: ['email' : "test@tersuslavanderia.com"]).data
            println "Initial" + new JsonBuilder(sodClient).toPrettyString()

            // update client type
            restClient.post(path: "clients/clientType/addClients/2", body: sodClient, requestContentType: MediaType.JSON_UTF_8)

            // get client again..
            sodClient = restClient.get(path: "clients", query: ['email' : "test@tersuslavanderia.com"])
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
        // get client
        def sodClient = restClient.get(path: "clients", query: ['email' : "test@tersuslavanderia.com"])

        then: "The correct status is expected. "
        with(sodClient) {
            status == HttpStatus.SC_OK
            println "Initial " + new JsonBuilder(data).toPrettyString()
            data.idClientType == 3
        }

    }
}