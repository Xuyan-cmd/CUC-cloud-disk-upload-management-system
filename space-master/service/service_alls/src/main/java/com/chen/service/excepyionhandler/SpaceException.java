package com.chen.service.excepyionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceException extends RuntimeException {

    private Integer code;
    private String msg;
}
