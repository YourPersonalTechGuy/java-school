package com.lambdaschool.schools.services;

import com.lambdaschool.schools.models.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunction")
public class HelperFunctionsImpl implements HelperFunctions{

    @Override
    public List<ValidationError> getValidationErrors(Throwable cause) {

        //Create a Validation error list to store any errors in
        List<ValidationError> validationErrorList = new ArrayList<>();

        //ConstraintViolationExceptions are Jackson Catches while MethodArgumentNotValidException are hibernate catches
        //while there is a cause and the cause is either a jackson error or a hibernate error then...
        while(cause != null && !(cause instanceof ConstraintViolationException || cause instanceof MethodArgumentNotValidException)){
            cause = cause.getCause();
        }

        //If there is an error then...
        if(cause != null){

            //If there is an error in relation to the jackson side of Spring then...
            if (cause instanceof ConstraintViolationException){

                //Create a new instance of error type for jackson and cast the cause as it
                ConstraintViolationException newException = (ConstraintViolationException)cause;

                //Create new validation error object
                ValidationError newValidationError = new ValidationError();
                //In the newly created object set the message with the type casted cause ConstraintName name which should correlate to the message in our validation error object
                newValidationError.setMessage(newException.getConstraintName());
                //In the newly created object set the fieldname with the type casted cause Message name which should correlate to the fieldname in our validation error object
                newValidationError.setFieldname(newException.getMessage());

                //add the new validation error in the list of validation errors that we have
                validationErrorList.add(newValidationError);
            } else {

                //Create a new instance of error type for hibernate and cast the cause as it
                MethodArgumentNotValidException newException = (MethodArgumentNotValidException)cause;

                //Hibernate errors are stored as a list
                //Create a new List of type FieldError and store list of field errors from the object of error type hibernate
                List<FieldError> fieldErrors = newException.getBindingResult().getFieldErrors();

                //Loop over each of the errors then...
                for(FieldError fe : fieldErrors){

                    //Create new validation error object
                    ValidationError newValidationError = new ValidationError();
                    //In the newly created object set the FieldName from the FieldError's Field which should correlate to the FieldName in our validation error object
                    newValidationError.setFieldname(fe.getField());
                    //In the newly created object set the Message from the FieldError's DefaultMessage which should correlate to the message in our validation error object
                    newValidationError.setMessage(fe.getDefaultMessage());

                    //add the new validation error in the list of validation errors that we have
                    validationErrorList.add(newValidationError);
                }
            }
        }

        return validationErrorList;
    }

}
