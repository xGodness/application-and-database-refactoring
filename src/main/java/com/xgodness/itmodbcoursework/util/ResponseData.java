package com.xgodness.itmodbcoursework.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    List<String> errorList;
    List<?> resultList;
}
