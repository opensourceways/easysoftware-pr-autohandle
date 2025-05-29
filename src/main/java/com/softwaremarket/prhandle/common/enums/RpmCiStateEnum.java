package com.softwaremarket.prhandle.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RpmCiStateEnum {
    /**
     * ci_failed .
     */
    CI_FAIL("ci_failed", "CI失败"),
    /**
     * ci_processing .
     */
    CI_PROCESSING("ci_processing", "ci 进行中"),
    /**
     * ci_successful .
     */
    CI_SUCCESS("ci_successful", "CI成功");
    /**
     * 英文名称 .
     */
    private final String name;

    /**
     * 解释信息 info .
     */
    private final String msg;
}
