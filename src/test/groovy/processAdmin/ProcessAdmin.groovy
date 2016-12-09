package processAdmin

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by cesaregb on 9/24/16.
 */

class ProcessAdmin{

    def someMethod(){

	    println "something.... "

	    Client client = ClientBuilder.newClient();
	    WebTarget target = client.target("http://localhost:9000/api").path("appContext").path("neta")

	    Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get()

	    println "Status: " + response.getStatus();

	    def jsonSlurper = new JsonSlurper()
	    def object = jsonSlurper.parseText(response.readEntity(String.class))

	    println new JsonBuilder( object ).toPrettyString()

    }

	public static void main(String[] args) {
		ProcessAdmin pa = new ProcessAdmin();
		pa.someMethod();
	}
}