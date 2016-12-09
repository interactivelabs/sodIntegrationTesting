package sod

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by cesaregb on 10/31/16.
 */
class BaseSodSpecification  extends Specification{

    void setup() {
        restClient = new RESTClient("http://localhost:8080/api/")
        restClient.handler.failure = restClient.handler.success

        uris = URIs.uris;
//		println("*******************")
//		println("*******************")
//		println("*******************")
//	    uris.each{ k, v -> println "${k}:${v}" }
//	    println("*******************")
//	    println("*******************")
//	    println("*******************")

        println("myProperty: " + Helper.getProperties())
    }

    @Shared
    def restClient = null;

    @Shared
    def uris
}
