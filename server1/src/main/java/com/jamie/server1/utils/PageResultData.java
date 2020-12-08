package com.jamie.server1.utils;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/7 17:38
 * @description
 */
@Data
public class PageResultData<T> implements Serializable {
    private String returnCode;
    private String returnStatus;
    private PageInfo<T> pageInfo;
}
