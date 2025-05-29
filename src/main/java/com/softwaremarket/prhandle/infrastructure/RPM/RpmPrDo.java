package com.softwaremarket.prhandle.infrastructure.RPM;

import lombok.Data;

@Data

public class RpmPrDo {
    /**
     * 仓库拥有者.
     */
    private String owner;
    /**
     * 仓库名称.
     */
    private String repo;
    /**
     * pr number.
     */
    private Integer number;

    /**
     * pr html 链接.
     */
    private String htmlUrl;
}
