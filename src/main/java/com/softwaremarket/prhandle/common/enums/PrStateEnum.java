package com.softwaremarket.prhandle.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrStateEnum {


    /**
     * open .
     */
    OPEN("open", "打开"),

    /**
     * merged .
     */
    MERGED("merged", "合并"),

    /**
     * closed .
     */
    CLOSED("closed", "关闭"),
    /**
     * all .
     */
    ALL("all", "所有"),
    ;
    /**
     * 英文状态 .
     */
    private final String state;

    /**
     * 解释信息 info .
     */
    private final String msg;
}
