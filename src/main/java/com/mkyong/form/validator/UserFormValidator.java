package com.mkyong.form.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mkyong.form.model.User;

@Component
public class UserFormValidator implements Validator{
	
	@Autowired
	@Qualifier("emailValidator")
	EmailValidator emailValidator;
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz){
		return User.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors){
		
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "name", "NotEmpty.userForm.name");
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "email", "NotEmpty.userForm.email");
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "address", "NotEmpty.userForm.address");
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "password", "NotEmpty.userForm.password");
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "confirmPassword", "NotEmpty.userForm.confirmPassword");
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "sex", "NotEmpty.userForm.sex");
		ValidationUtils.rejectIfEmptyOrwhitespace(errors, "country", "NotEmpty.userForm.country");
		
		if(!emailValidator.valud(user.getEmail())){
			errors.rejectValue("email", "Pattern.userForm.email");
		}
		
		if(user.getNumber() ==null || user.getNumber()<=0){
			errors.rejectValue("number", "Notempty.userForm.number");
		}
		
		if(user.getCountry().equalsIgnoreCase("non")){
			errors.rejectValue("country", "Notempty.userForm.country");
		}
		if(!user.getPassword().equals(user.getConfirmPassword())){
			errors.rejectValue("confirmedPassword", "Diff.userform.confirmPassword");
		}
		if(user.getFramework() ==null || user.getFramework().size() < 2){
			errors.rejectValue("framework", "Valid.userForm.framework");
		}
		if(user.getSkill() ==null || user.getSkill().size() < 3) {
			errors.rejectValue("skill", "Valid.userForm.skill");
		}
		
	}

}
