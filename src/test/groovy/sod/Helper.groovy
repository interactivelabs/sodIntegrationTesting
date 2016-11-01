package sod

/**
 * Created by cesaregb on 9/25/16.
 */
class Helper {
    def static getProperties(){
        def myProperty = (System.getenv("myProperty"))?System.getenv("myProperty"):"value"
        return myProperty;
    }
}
