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
// Operator information, the repository will be forked to this user's repository
// most of the information can be found in the Gitee personal center
public class ForkInfo {
    /**
     *  The access token for the user whose repository the code will be forked to, and subsequent operations will be performed using this user
     */
    String accessToken;

    /**
     * The owner of the repository
     */
    String owner;

    /**
     * The email associated with the repository
     */
    String email;

    /**
     * The name of the repository
     */
    String name;

    /**
     * The password for the repository, used to log in and obtain the token
     */
    String password;

    /**
     * The scope of the account permissions
     */
    String scope;

    /**
     * The client ID required for Gitee login, in addition to the password
     */
    String clientId;

    /**
     * The client secret required for Gitee login, in addition to the password
     */
    String clientSecret;
}
