package com.alex.model;

import lombok.Data;

import javax.validation.constraints.*;
import javax.validation.groups.Default;

@Data
public class Employee {

    @NotNull(groups = {GroupA.class})
    @Null(groups = {GroupB.class})
    private Integer id;

    @NotBlank(groups = {GroupB.class})
    private String name;

    @NotNull(groups = {GroupB.class})
    private String alex;

    @Max(value = 800, groups = {GroupA.class})
    private Integer max;

    @Min(value = 0, groups = {GroupB.class})
    private Integer age;

    @NotBlank(groups = {Default.class})
    private String def;


}

