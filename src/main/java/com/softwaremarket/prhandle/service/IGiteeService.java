package com.softwaremarket.prhandle.service;

import com.alibaba.fastjson.JSONObject;
import com.gitee.sdk.gitee5j.model.*;
import com.softwaremarket.prhandle.common.config.properties.ForkInfo;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface IGiteeService {
    JSONObject fork(Map parameter);


    PullRequest createPullRequest(RepoPullsBody body, String token, String owner, String repo);

    Issue createIssue(String token, String owner, OwnerIssuesBody body);

    List<JSONObject> getV5ReposOwnerRepoPulls(String url);

    List<JSONObject> getContents(String owner, String repo, String path, String token, String branch);

    Tree getReposOwnerRepoGitTreesSha(String token, String owner, String repo, String sha, Integer recursive);

    File getReposOwnerRepoRawPath(String token, String owner, String repo, String path, String ref);

    RepoCommitWithFiles postReposOwnerRepoCommits(String token, String owner, String repo, RepoCommitsBody body);

    PullRequest postReposOwnerRepoPulls(String token, String owner, String repo, RepoPullsBody body);


    CompleteBranch postReposOwnerRepoBranches(String token, String owner, String repo, RepoBranchesBody body);


    HashSet<String> getReposProjects(String repo, String token);

    String getTokenByPassword(ForkInfo forkInfoDto);

    Boolean deleteRepos(String token, String owner, String repo);

    List<JSONObject> getLabels(String owner, String repo, String number, String token, Integer page, Integer perPage);

    public void updatePrState(String apiUrl, String state, String accessToken);

    public void upPrComments(String token, String owner, String repo, Integer number, String body);

    public JSONObject getPrInfoByNum(String token, String owner, String repo, Integer number);


    public List<JSONObject> getPrComments(String token, String owner, String repo, Integer number);
}
