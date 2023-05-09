package com.example.demo.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@Data
public class Employee {

    @NotNull(groups = {GroupA.class})
    private Long id;

    @NotBlank(groups = {GroupB.class})
    private String name;

    @Min(value = 10, groups = {GroupC.class})
    private Integer age;

    private int gender;

    @NotBlank(groups = {GroupD.class})
    private String phone;

    @NotBlank(groups = {Default.class})
    private String address;

}
interface GroupA{}
interface GroupB{}
interface GroupC{}
interface GroupD{}