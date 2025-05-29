/* Copyright (c) 2024 openEuler Community
 EasySoftwareInput is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.softwaremarket.prhandle.common.config.properties;

import lombok.Data;

@Data
public class PrInfo {
    /**
     * the pull request tittle.
     */
    private String prTitle;
    /**
     * the issue number.
     */
    private String issueNum;
    /**
     * changelog.
     */
    private String changelog;
    /**
     * epoch changelog.
     */
    private String epochChangelog;
}
