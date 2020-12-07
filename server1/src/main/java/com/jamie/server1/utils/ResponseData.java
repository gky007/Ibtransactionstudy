package com.jamie.server1.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 17:25
 * @description
 */
@Data
public class ResponseData<T> implements Serializable {
    private Object data;
    private String returnCode;
    private String returnStatus;
}
