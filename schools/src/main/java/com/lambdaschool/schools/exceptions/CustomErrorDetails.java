package com.lambdaschool.schools.exceptions;

import com.lambdaschool.schools.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorDetails extends DefaultErrorAttributes {

    /*
    title: string
    status: int ex: 400, 404, 500, etc.
    detail: string (one sentence summary)
    timestamp: date
    developerMessage: string ex: com.lambdaschool.schools.exceptions.ResourceNotFoundException
     */

    @Autowired
    private HelperFunctions helperFunctions;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> originErrorAttributes =  super.getErrorAttributes(webRequest, includeStackTrace);

        Map<String, Object> returnAttributes = new LinkedHashMap<>();

        returnAttributes.put("title", originErrorAttributes.get("error"));
        returnAttributes.put("status", originErrorAttributes.get("status"));
        returnAttributes.put("detail", originErrorAttributes.get("message"));
        returnAttributes.put("timestamp", new Date());
        returnAttributes.put("developerMessage", "path: " + originErrorAttributes.get("path"));


        returnAttributes.put("errors", helperFunctions.getValidationErrors(this.getError(webRequest)));

        return returnAttributes;
    }


}
