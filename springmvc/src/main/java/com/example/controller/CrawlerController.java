package com.example.controller;

import com.example.dao.Jdbc;
import com.example.model.DBCrawlerGitDetailDataModel;
import com.example.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jianjun Lv
 * @date 2018/5/17 13:35
 */
// 注解标注此类为springmvc的controller，url映射为"/home"
@Controller
@RequestMapping("/crawler")
public class CrawlerController {
    //添加一个日志器
    private static final Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    //映射一个action
    @RequestMapping(value = "/index", produces = "application/json; charset=utf-8")
    public ModelAndView index(
            @RequestParam(value = "repositoryPath", required = false, defaultValue = "") String repositoryPath,
            @RequestParam(value = "downloadURL", required = false, defaultValue = "") String downloadURL,
            @RequestParam(value = "repositoryContent", required = false, defaultValue = "") String repositoryContent,
            @RequestParam(value = "topicsList1", required = false, defaultValue = "") String topicsList1,
            @RequestParam(value = "topicsList2", required = false, defaultValue = "") String topicsList2,
            @RequestParam(value = "topicsList3", required = false, defaultValue = "") String topicsList3,
            @RequestParam(value = "topicsList4", required = false, defaultValue = "") String topicsList4
    ) {
        //输出日志文件
        logger.info("the first jsp pages");
        String repositoryPathText = repositoryPath.trim().replaceAll("'", "''");
        String downloadURLText = downloadURL.trim().replaceAll("'", "''");
        String repositoryContentText = repositoryContent.trim().replaceAll("'", "''");
        String[] topicsList = new String[]{topicsList1.trim().replaceAll("'", "''"),
                topicsList2.trim().replaceAll("'", "''"),
                topicsList3.trim().replaceAll("'", "''"),
                topicsList4.trim().replaceAll("'", "''")};
        if (repositoryPathText.equals("") &&
                downloadURLText.equals("") &&
                repositoryContentText.equals("") &&
                topicsList[0].equals("") &&
                topicsList[1].equals("") &&
                topicsList[2].equals("") &&
                topicsList[3].equals("")) {
            return new ModelAndView("indexEmpty");
        }
        ModelAndView mav = new ModelAndView("indexResult");
        mav.addObject("repositoryPath", repositoryPath);
        mav.addObject("downloadURL", downloadURL);
        mav.addObject("repositoryContent", repositoryContent);
        mav.addObject("topicsList1", topicsList1);
        mav.addObject("topicsList2", topicsList2);
        mav.addObject("topicsList3", topicsList3);
        mav.addObject("topicsList4", topicsList4);
        int pageCountInt;
        CrawlerService service = new CrawlerService();
        List<DBCrawlerGitDetailDataModel> modelList;
        try {
            modelList = service.GetExistReadmeData(repositoryPathText, downloadURLText, repositoryContentText, topicsList);
        } catch (SQLException e) {
            modelList = new ArrayList<>();
        }
        pageCountInt = modelList.size();
        for (int i = 0; i < pageCountInt; i++) {
            modelList.get(i).setIndex(i + 1);
        }
        if (pageCountInt == 0) {
            mav.addObject("pageIndexInt", 0);
            mav.addObject("pageCountInt", 0);
            mav.addObject("noDataInfo", "There is no compliant data!!!");
            return mav;
        }
        mav.addObject("noDataInfo", "");
        mav.addObject("pageIndexInt", 1);
        mav.addObject("pageCountInt", pageCountInt);

        List<String> repositoryPathList = new ArrayList<>(pageCountInt);
        List<String> downloadURLList = new ArrayList<>(pageCountInt);
        List<Integer> impressionCountList = new ArrayList<>(pageCountInt);
        List<Integer> clickCountList = new ArrayList<>(pageCountInt);
        List<String> repositoryContentList = new ArrayList<>(pageCountInt);
        List<String> topicsListContentList = new ArrayList<>(pageCountInt);
        List<String> readmeFileContentList = new ArrayList<>(pageCountInt);

        boolean start = true;
        for (DBCrawlerGitDetailDataModel model : modelList) {
            repositoryPathList.add(model.getRepositoryPath());
            downloadURLList.add(model.getDownloadURL());
            impressionCountList.add(model.getImpressionCount());
            clickCountList.add(model.getClickCount());
            repositoryContentList.add(model.getRepositoryContent());
            String topicsListContent = "";
            String[] topicsListCur = model.getTopicsList();
            for (String topic : topicsListCur) {
                topicsListContent += topic + "\t";
            }
            topicsListContentList.add(topicsListContent);

            readmeFileContentList.add(model.getReadmeFileContent());

            if (start) {
                mav.addObject("repositoryPathResult", model.getRepositoryPath());
                mav.addObject("downloadURLResult", model.getDownloadURL());
                mav.addObject("impressionCountResult", model.getImpressionCount());
                mav.addObject("clickCountResult", model.getClickCount());
                mav.addObject("repositoryContentResult", model.getRepositoryContent());
                mav.addObject("topicsListResult", topicsListContent);
                mav.addObject("readmeContentResult", model.getReadmeFileContent());
                start = false;
            }
        }

        mav.addObject("repositoryPathList", repositoryPathList);
        mav.addObject("downloadURLList", downloadURLList);
        mav.addObject("impressionCountList", impressionCountList);
        mav.addObject("clickCountList", clickCountList);
        mav.addObject("repositoryContentList", repositoryContentList);
        mav.addObject("topicsListContentList", topicsListContentList);
        mav.addObject("readmeFileContentList", readmeFileContentList);
        mav.addObject("modelList", modelList);
        return mav;
    }

    @RequestMapping("/index2")
    public @ResponseBody
    String index2() {
        Jdbc j = new Jdbc();

        return j.testConn();
    }

    @RequestMapping("/index3")
    String index3() {
        return "index";
    }

    @RequestMapping("/index4")
    public @ResponseBody
    String index4() {
        try {
            CrawlerService service = new CrawlerService();
            List<DBCrawlerGitDetailDataModel> modelList;
            modelList = service.GetExistReadmeData("", "", "", new String[]{"caffe"});
            String res = "";
            for (DBCrawlerGitDetailDataModel model : modelList) {
                res += model.getRepositoryPath() + "\n\t";
            }
            return res;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}