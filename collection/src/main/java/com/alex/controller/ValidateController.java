package com.alex.controller;

import com.alex.common.Response;
import com.alex.model.Employee;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.groups.Default;

@RestController()
@RequestMapping(value = "validate")
public class ValidateController {

    @RequestMapping(method = RequestMethod.POST,value = "demo")
    public Response validatedController(@RequestBody @Validated() Employee employee){
        Response response = new Response();
        System.out.println(employee);
        response.setCode("200");
        response.setMessage("success");
        return response;
    }

}
