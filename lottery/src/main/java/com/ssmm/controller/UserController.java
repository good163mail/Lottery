package com.ssmm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ssmm.model.User;
import com.ssmm.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value="/add" )
	public ModelAndView add() {
		ModelAndView view = new ModelAndView("add");
		//view.setViewName("add");
		return view;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody User showOneUser(@PathVariable("id") int id) {

		User user = userService.getUserById(id);
		return user;
	}
}
