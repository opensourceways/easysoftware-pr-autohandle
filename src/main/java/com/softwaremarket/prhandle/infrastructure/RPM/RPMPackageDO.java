/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.softwaremarket.prhandle.infrastructure.rpm;

import lombok.Data;

@Data

public class RPMPackageDO {
    /**
     * the name of rpm package.
     */
    private String name;
    /**
     * the upstream url of rpm package.
     */
    private String upstreamUrl;
    /**
     * the regular expression of rpm package which used to match the version
     */
    private String regex;
    /**
     * autoupgrade enable
     */
    private String autoUpgrade;

    /**
     * changelog url
     */
    private String changelog;

}
