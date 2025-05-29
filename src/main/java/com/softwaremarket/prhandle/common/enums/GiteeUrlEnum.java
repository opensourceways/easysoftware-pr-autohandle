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
package com.softwaremarket.prhandle.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GiteeUrlEnum {
    PostV5ReposOwnerRepoForksUrl("https://gitee.com/api/v5/repos/%s/%s/forks", "fork"),
    GiteeGetV5ReposOwnerRepoPullsUrl("https://gitee.com/api/v5/repos/{owner}/{repo}/pulls?access_token={access_token}&state=all&sort=created&direction=desc&page={page}&per_page=100&state={state}", "批量获取该仓库提交的pr"),
    ContentsUrl("https://gitee.com/api/v5/repos/{owner}/{repo}/contents/{path}?access_token={access_token}&ref={ref}", "获取文件内容"),
    DeleteReposUrl("https://gitee.com/api/v5/repos/{owner}/{repo}", "删除仓库"),
    GetTokenUrl("https://gitee.com/oauth/token", "获取token"),
    ReposInfoUrl("https://gitee.com/api/v5/orgs/{org}/repos?access_token={token}&type=all&per_page=20&page=", "获取仓库信息"),
    PrINfoByNum("https://gitee.com/api/v5/repos/{owner}/{repo}/pulls/{number}?access_token={access_token}", "获取pr"),
    PrAllComments("https://gitee.com/api/v5/repos/{owner}/{repo}/pulls/{number}/comments?access_token={access_token}&per_page=20&page={page}", "获取pr所有评论"),
    PrUpdate("https://gitee.com/api/v5/repos/{owner}/{repo}/pulls/{number}", "更新pr");
    private final String url;
    private final String description;
}
