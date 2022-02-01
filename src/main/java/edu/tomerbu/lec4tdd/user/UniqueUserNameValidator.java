package edu.tomerbu.lec4tdd.user;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//the validator validates for our annotation :UniqueUserName
//userName is a String
public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

    @Autowired
    UserRepository repository;

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        User inDb = repository.findByUserName(userName);
        return inDb == null;//if no user => valid(TRUE), else invalid.
    }
}
