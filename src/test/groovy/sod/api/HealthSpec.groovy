package sod.api

import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by cesaregb on 9/24/16.
 */
class HealthSpec extends Specification {

    @Shared
    def client = new RESTClient("http://localhost:8080/api/")

    def "Test Services be alive..."() {
        when: "Call made to simple endpoint"
        def response = client.get(path : "health")

        println new JsonBuilder( response.data ).toPrettyString()

        then: "the correct message is expected"
        with(response) {
            status == 200
        }
    }

}