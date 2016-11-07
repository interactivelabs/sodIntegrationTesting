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
class TasksSpec extends BaseSodSpecification{
    @Shared
    String path = "tasks";

    @Shared
    String pathType = "tasks/task-type";

    @Shared
    String nameHelper = "test-" + RandomStringUtils.randomAlphanumeric(10);

    void setup() {
        println("path: "+path+" \n nameHelper: " + nameHelper);
    }

    def "Add 2 Tasktype + Add Task + change task from task type "() {
        when: "Create 1st type"
        def body = [
                "description": "description",
                "name": "type" + nameHelper,
                "ordersOnly": false
        ];
        def response = restClient.post(path: pathType, body: body, requestContentType: MediaType.JSON_UTF_8)
        def type1 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create 2nd type"
        body = [
                "description": "description2",
                "name": "type2" + nameHelper,
                "ordersOnly": false
        ];
        response = restClient.post(path: pathType, body: body, requestContentType: MediaType.JSON_UTF_8)
        def type2 = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

        when: "Create task"
        body = [
                "description": "description",
                "name": nameHelper,
                "idTaskType": type1.idTaskType
        ];
        response = restClient.post(path: path, body: body, requestContentType: MediaType.JSON_UTF_8)
        def child = response.data;
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }

	    when: "Update Task"
	    def updatePath = new StringBuilder(pathType).append("/").append(type2.idTaskType).append("/child/").append(child.idTask);
	    response = restClient.put(path: updatePath, requestContentType: MediaType.JSON_UTF_8)
	    println new JsonBuilder(response.data).toPrettyString()

	    then:
	    with(response) {
		    status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
	    }

	    when: "Update Task 2"
	    body = child;
	    body.idTaskType = type1.idTaskType;
	    response = restClient.put(path: path, body: body, requestContentType: MediaType.JSON_UTF_8)
	    println new JsonBuilder(response.data).toPrettyString()

	    then:
	    with(response) {
		    status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
	    }

        when: "Delete Parents"
        restClient.delete(path: new StringBuilder(pathType).append("/").append(+type1.idTaskType), requestContentType: MediaType.JSON_UTF_8)
        response = restClient.delete(path: new StringBuilder(pathType).append("/").append(+type2.idTaskType), requestContentType: MediaType.JSON_UTF_8)
        println new JsonBuilder(response.data).toPrettyString()

        then:
        with(response) {
            status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED
        }
    }

}
