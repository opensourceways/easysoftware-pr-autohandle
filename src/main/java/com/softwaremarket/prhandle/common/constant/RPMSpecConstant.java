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
package com.softwaremarket.prhandle.common.constant;

public class RPMSpecConstant {
    /**
     * The defaul release version number of rpm
     */
    public static final String DEAFAULT_RELEASE = "1";

    /**
     * The token used to distinguish epoch changelog from normal changelog.
     */
    public static final String EPOCH_FORMAT_CHANGELOG_TOKEN = ":";

    /**
     * The token used to locate where changelogs begin
     */
    public static final String CHANGELOG_TOKEN = "%changelog";


    /**
     * The token used to locate where version begin
     */
    public static final String VERSION_TOKEN = "Version";


    /**
     * The token used to locate where version begin
     */
    public static final String REALEASE_TOKEN = "Release:";


    /**
     * The token used to locate where url begin
     */
    public static final String URL_TOKEN = "URL:";


    /**
     * The default string
     */
    public static final String DEFAULT = "default";
}
