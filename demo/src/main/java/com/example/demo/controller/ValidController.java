package com.example.demo.controller;

import com.example.demo.annotation.CheckParam;
import com.example.demo.jvm.JVMTest;
import com.example.demo.model.Employee;
import com.example.demo.util.SignUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "valid")
public class ValidController {

    @RequestMapping(value = "first")
    public String first(@RequestBody @Validated Employee employee) {

        System.out.println(employee);
        return "success";
    }

    //http://localhost:8888/collection/valid/random?length=5
    @RequestMapping(value = "random", method = RequestMethod.GET)
    public String random(@RequestParam("length") int length) {
        final String nonceStr = SignUtil.getNonceStr(length);
        return "success:" + nonceStr;
    }

    //http://localhost:8888/collection/valid/fillHeap/5
    @RequestMapping(value = "fillHeap/{num}", method = RequestMethod.GET)
    public String fillHeap(@PathVariable("num") int num) {
        try {
            JVMTest.fillHeap(num);
            System.gc();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "exception";
        }
        return "success";

    }

    public static void main(String[] args) {
        String str = "31010819870310361";
        int[] coeff = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        char[] suffix = new char[]{'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += coeff[i] * Integer.parseInt(String.valueOf(str.charAt(i) - 48));
        }
        sum %= 11;
        str = str.substring(0, 17) + suffix[sum];
        System.out.println(str);

    }

}
