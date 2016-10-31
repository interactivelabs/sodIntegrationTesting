package sod

import com.google.common.base.CaseFormat
import io.swagger.models.Swagger
import io.swagger.parser.SwaggerParser

/**
 * Created by cesaregb on 9/27/16.
 */
class URIs {

    public static def uris = [:];

    static{
        Swagger swagger = new SwaggerParser().read("http://localhost:8080/api/swagger.json");
        swagger.paths.each {k , v->

            String keyUpdated = k.substring(1, k.length())
            if (!keyUpdated.contains("{")){
                String[] parts = k.split("/");
                keyUpdated = parts[parts.length -1];
                if (keyUpdated.contains("-")){
                    keyUpdated = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, keyUpdated);
                }
                uris.put(keyUpdated, k);
            }
        }
    }
}
