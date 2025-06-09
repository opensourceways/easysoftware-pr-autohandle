package com.softwaremarket.prhandle.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.softwaremarket.prhandle.common.config.RpmConfig;
import com.softwaremarket.prhandle.common.config.properties.ForkInfo;
import com.softwaremarket.prhandle.common.constant.RemoteConfigConstant;
import com.softwaremarket.prhandle.common.enums.GiteeRepoEnum;
import com.softwaremarket.prhandle.common.enums.GiteeUrlEnum;
import com.softwaremarket.prhandle.common.enums.PrStateEnum;
import com.softwaremarket.prhandle.common.enums.RpmCiStateEnum;
import com.softwaremarket.prhandle.infrastructure.RPM.RpmPrDo;
import com.softwaremarket.prhandle.service.IGiteeService;
import com.softwaremarket.prhandle.util.FileUtil;
import com.softwaremarket.prhandle.util.HttpRequestUtil;
import com.softwaremarket.prhandle.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class RpmPrHandleTask {


    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RpmPrHandleTask.class);

    /**
     * config of rpm.
     */
    @Autowired
    private RpmConfig rpmConfig;

    /**
     * service of git.
     */
    @Autowired
    protected IGiteeService gitService;

    /**
     * auto upgrade rpm version.
     */
    public void closeCifailedPr() {
        this.getToken(rpmConfig.getForkInfo());

        File file = gitService.getReposOwnerRepoRawPath(rpmConfig.getForkInfo().getAccessToken(),
                RemoteConfigConstant.REMOTE_CONFIG_OWNER,
                RemoteConfigConstant.REMOTE_CONFIG_REPO,
                RemoteConfigConstant.REMOTE_CONFIG_PATH,
                RemoteConfigConstant.REMOTE_CONFIG_BRANCH);
        List<com.softwaremarket.prhandle.infrastructure.rpm.RPMPackageDO> rpmPackages = FileUtil.getRPMFromYamlList(file.getPath());
        // List<com.softwaremarket.prhandle.infrastructure.rpm.RPMPackageDO> rpmPackages = FileUtil.getRPMFromYamlList(file.getPath());
        ArrayList<RpmPrDo> rpmPrRepairList = new ArrayList<>();
        for (com.softwaremarket.prhandle.infrastructure.rpm.RPMPackageDO rpmPackage : rpmPackages) {
            try {
                String name = rpmPackage.getName();
                getNeedRepairPr(GiteeRepoEnum.RPM.getOwner(), name, rpmConfig.getForkInfo().getAccessToken(), rpmPrRepairList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("-----开始pr处理！！！ \n");
        StringBuilder logBuilder = new StringBuilder();
        for (RpmPrDo rpmPrDo : rpmPrRepairList) {
            logBuilder.append(rpmPrDo.getHtmlUrl()).append("\n");
        }
        // 日志记录处理了哪些pr;
        log.info(logBuilder.toString());
        // mpich   jimtcl neon nim rpmlint  swig  tbb  perl-PPI  perl-MooseX-Types-Common  perl-MooseX-Types perl-MooseX-SetOnce
        if (rpmPrRepairList.isEmpty())
            return;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                5, // 核心线程数
                5, // 最大线程数
                60, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>() // 任务队列
        );
        for (RpmPrDo rpmPrDo : rpmPrRepairList) {
            threadPool.execute(() -> {
                Boolean aBoolean;
                for (int i = 0; i < 10; i++) {
                    aBoolean = repairPrOrClose(rpmPrDo, rpmConfig.getForkInfo().getAccessToken());
                    if (aBoolean) {
                        break;
                    }
                    log.info(rpmPrDo.getHtmlUrl() + " 执行完毕");
                }
            });
        }


        // 主线程等待 threadPool线程池中所有任务完成
        while (threadPool.getActiveCount() > 0) {
            try {
                Thread.sleep(10000); // 避免忙等待
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //获取所有仓库下需要repair的pr
    protected void getNeedRepairPr(String owner, String repo, String token, ArrayList<RpmPrDo> rpmPrRepairList) {
        List<JSONObject> v5ReposOwnerRepoPulls = new ArrayList<>();
        int page = 0;
        String url = GiteeUrlEnum.GiteeGetV5ReposOwnerRepoPullsUrl.getUrl().replace("{owner}", owner).replace("{repo}", repo).replace("{access_token}", token).replace("{state}", PrStateEnum.OPEN.getState());
        do {
            page++;
            String replace = url.replace("{page}", String.valueOf(page));
            List<JSONObject> pr = gitService.getV5ReposOwnerRepoPulls(replace);
            if (pr != null)
                v5ReposOwnerRepoPulls.addAll(pr);
        } while (v5ReposOwnerRepoPulls.size() == page * 100);

        if (CollectionUtils.isEmpty(v5ReposOwnerRepoPulls))
            return;

        for (JSONObject v5ReposOwnerRepoPull : v5ReposOwnerRepoPulls) {
            JSONObject user = v5ReposOwnerRepoPull.getJSONObject("user");
            String name = user.getString("name");
            String state = v5ReposOwnerRepoPull.getString("state");
            Integer number = v5ReposOwnerRepoPull.getInteger("number");
            String htmlUrl = v5ReposOwnerRepoPull.getString("html_url");
            if (rpmConfig.getForkInfo().getName().equals(name) && PrStateEnum.OPEN.getState().equals(state)) {
                //开始判断状态
                JSONArray labels = v5ReposOwnerRepoPull.getJSONArray("labels");
                if (labels == null)
                    continue;

                for (int i = 0; i < labels.size(); i++) {
                    JSONObject label = labels.getJSONObject(i);
                    String labelName = label.getString("name");
                    // ci 失败或者ci 正在进行中的都需要再次检查;
                    if (RpmCiStateEnum.CI_FAIL.getName().equals(labelName) || RpmCiStateEnum.CI_PROCESSING.getName().equals(labelName)) {
                        RpmPrDo rpmPrDto = new RpmPrDo();
                        rpmPrDto.setNumber(number);
                        rpmPrDto.setOwner(owner);
                        rpmPrDto.setRepo(repo);
                        rpmPrDto.setHtmlUrl(htmlUrl);
                        rpmPrRepairList.add(rpmPrDto);
                        break;
                    }
                }


            }
        }
    }

    // true 代码退出；false 代表继续执行
    public Boolean repairPrOrClose(RpmPrDo rpmPrDo, String token) {
        try {
            JSONObject prInfo = gitService.getPrInfoByNum(token, rpmPrDo.getOwner(), rpmPrDo.getRepo(), rpmPrDo.getNumber());
            if (prInfo != null) {
                JSONArray labels = prInfo.getJSONArray("labels");
                if (labels == null)
                    return Boolean.TRUE;
                // 判断当前标签中是否已经ci_success
                for (int i = 0; i < labels.size(); i++) {
                    JSONObject label = labels.getJSONObject(i);
                    String labelName = label.getString("name");
                    //ci 成功则退出
                    if (RpmCiStateEnum.CI_SUCCESS.getName().equals(labelName)) {
                        return Boolean.TRUE;
                    }

                    if (RpmCiStateEnum.CI_PROCESSING.getName().equals(labelName)) {
                        Thread.sleep(600000);
                        // 如果ci正在进行中则先退出；
                        return Boolean.FALSE;
                    }
                }
                // 获取pr所有评论.
                List<JSONObject> prComments = gitService.getPrComments(token, rpmPrDo.getOwner(), rpmPrDo.getRepo(), rpmPrDo.getNumber());
                Integer repairNum = 0;

                Integer lastestRepairNum = 0;
                Integer lastestResetNum = 0;
                for (int i = 0; i < prComments.size(); i++) {
                    JSONObject prComment = prComments.get(i);
                    String body = prComment.getString("body").trim();
                    String name = prComment.getJSONObject("user").getString("name");
                    if (rpmConfig.getForkInfo().getName().equals(name)) {
                        //创建issue 则退出
                        if (body.contains("openEuler-AutoRepair已经提出Issue，请留意处理进度")) {
                            //关闭pr
                            gitService.updatePrState(GiteeUrlEnum.PrUpdate.getUrl().replace("{owner}", rpmPrDo.getOwner()).replace("{repo}", rpmPrDo.getRepo()).replace("{number}", rpmPrDo.getNumber().toString()), PrStateEnum.CLOSED.getState(), rpmConfig.getForkInfo().getAccessToken());
                            return Boolean.TRUE;
                        }
                        if ("/repair".equals(body)) {
                            repairNum++;
                            lastestRepairNum = i;
                        }
                    }
                    if ("openeuler-ci-bot".equals(name) && "/retest".equals(body)) {
                        lastestResetNum = i;
                    }
                }
                if (repairNum > 2) {
                    //repair 两次及以上并且cifail 关闭pr
                    gitService.updatePrState(GiteeUrlEnum.PrUpdate.getUrl().replace("{owner}", rpmPrDo.getOwner()).replace("{repo}", rpmPrDo.getRepo()).replace("{number}", rpmPrDo.getNumber().toString()), PrStateEnum.CLOSED.getState(), rpmConfig.getForkInfo().getAccessToken());

                    return Boolean.TRUE;
                }
                // 如果在/repair 之后openeuler-ci-bot有/retest 并且还不符合终结逻辑则继续提交/repair
                if (lastestResetNum >= lastestRepairNum)
                    gitService.upPrComments(token, rpmPrDo.getOwner(), rpmPrDo.getRepo(), rpmPrDo.getNumber(), "/repair");

                Thread.sleep(1200000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //如果因为某种意外原因导致出错,也暂时先退出;
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }


    public void getToken(ForkInfo forkInfo) {
        String tokenByPassword = gitService.getTokenByPassword(forkInfo);
        forkInfo.setAccessToken(tokenByPassword);
    }


    public List<JSONObject> getV5ReposOwnerRepoPulls(String url) {
        String result = HttpRequestUtil.sendGet(url);
        if (!StringUtils.isEmpty(result)) {
            return JacksonUtils.toObjectList(JSONObject.class, result);
        }
        return null;
    }
}
