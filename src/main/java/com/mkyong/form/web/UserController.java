package com.mkyong.form.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mkyong.form.model.User;
import com.mkyong.form.validator.UserFormValidator;

/**
 * 
 * @author ME.  1/26/2017 MVC tutorial
 * Using:  mkyong.com/spring-mvc/spring-mvc-form-handling-example/
 *
 */

@Controller
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserFormValidator userFormValidator;
	
	// Set a form validator
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(userFormValidator);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index(Model model){
		logger.debug("index()");
		return "redirect/users";
	}
	
	// list page
	@RequestMapping(value="/users", method = RequestMethod.GET)
	public String showAllUsers(Model model){
		
		logger.debug("showAllUsers");
		model.addAllAttributes("users", userService.findAll());
		return "users/list";
	}
	
	// save or update user
	// 1. @ModelAttribute bind form value
	// 2. @Validated form validator
	// 3. RedirectAttrributes for flash value
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String saveOrUpdateUser(@ModelAttribute("userForm") @Validated User user,
			BindingResult result, Model model,
			final RedirectAttributes redirectAttributes){
		
		logger.debug("saveOrUpdateUser() : {}", user);
		
		if(result.hasErrors()){
			populateDefaultModel(model);
			return "users/userform";
		}
		else {
			
			// Add message to flash scope
			redirectAttributes.addFlashAttribute("css" + "success");
			if(user.isNew()){
				redirectAttributes.addFlashAttribute("msg", "User added succesfully!");
			}else{
				redirectAttributes.addFlashAttribute("msg", "User updated succesfully!");
			}
			
			userService.saveOrUpdate(user);
			
			// POST/REDIRECT/GET
			return "redirect:/users/" + user.getId();
			
			// POST/FORWARD/GET
			// return "users/list"
		}
	}
	
	// show add user form
	@RequestMapping(value = "users/add", method = RequestMethod.GET)
	public String showAddUserForm(Model model){
		
		logger.debug("showAddUserForm()");
		
		User user = new User();
		
		// set default value
		user.setName("bruce123");
		user.setEmail("test@gmail.com");
		user.setAddress("abc 88");
		user.setNewsletter(true);
		user.setSex("M");
		user.setFramework(new ArrayList<String>(Arrays.asList("Spring MVC", "GWT")));
		user.setSkill(new ArrayList<String>(Arrays.asList("Spring", "Grails", "Groovy")));
		user.setCountry("US");
		user.setNumber(2);
		model.addAttribute("userForm", user);
		
		populateDefaultModel(model);
		
		return "users/userform";
	}
	
	// show update form
	@RequestMapping(value = "/users/{id}/update", method = RequestMethod.GET)
	public String showUpdateUserForm(@PathVariable("id") int id, Model model){
		
		logger.debug("showUpdateUserForm() : {}", id);
		
		User user = userService.findById(id);
		model.addAttribute("userForm", user);
		
		populateDefaultModel(model);
		
		return "users/userform";
	}
	
	// delete user
	@RequestMapping(value = "/users/{id}/delete", method = RequestMethod.POST)
	public String deleteUser(@PathVariable("id") int id, 
	final RedirectAttributes redirectAttributes){
		
		logger.debug("deleteUser() : {}", id);
		
		userService.delete(id);
		
		redirectAttributes.addFlashAttribute("Css", "success");
		redirectAttributes.addFlashAttribute("msg", "User is deleted!");
		
		return "redirect:/users";
		
	}
	
	// show user
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String showUser(@PathVariable("id") int id, Model model){
		
		logger.debug("showUser() id: {}", id);
		
		User user = userService.findById(id);
		if (user == null){
			model.addAttribute("css", "danger");
			model.addAttribute("msg", "User not found");
		}
		model.addAttribute("user", user);
		
		return "users/show";
	}
	
	private void populateDefaultModel(Model model){
		
		List<String> frameworksList = new ArrayList<String>();
		frameworksList.add("Spring MVC");
		frameworksList.add("Struts 2");
		frameworksList.add("JSF 2");
		frameworksList.add("GWT");
		frameworksList.add("Play");
		frameworksList.add("Apache Wicket");
		model.addAttribute("frameworkList", frameworksList);
		
		Map<String, String> skill = new LinkedHashMap<String, String>();
		skill.put("Hibernate", "Hibernate");
		skill.put("Spring", "Spring");
		skill.put("Struts", "Struts");
		skill.put("Groovy", "Groovy");
		skill.put("Grails", "Grails");
		model.addAttribute("javaSkillList", skill);
		
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		numbers.add(4);
		numbers.add(5);
		model.addAttribute("NumberList", numbers);
		
		Map<String, String> country = new LinkedHashMap<String, String>();
		country.put("US", "United Stated");
		country.put("CN", "China");
		country.put("SG", "Singapore");
		country.put("MY", "Malaysia");
		model.addAttribute("countryList", country);		
	
	}

}
