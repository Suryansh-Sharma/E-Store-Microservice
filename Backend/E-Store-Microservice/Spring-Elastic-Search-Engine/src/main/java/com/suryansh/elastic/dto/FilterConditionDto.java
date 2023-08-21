package com.suryansh.elastic.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FilterConditionDto{
    private Map<String, String> filters;
}
