package com.example.service;

import com.example.dao.Jdbc;
import com.example.model.DBCrawlerGitDetailDataModel;
import com.example.util.Constant;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CrawlerService {
    public List<DBCrawlerGitDetailDataModel> GetExistReadmeData(String repositoryPath, String downloadURL, String repositoryContent, String[] topicsList) throws SQLException {
        Connection connection;
        Statement statement;
        List<DBCrawlerGitDetailDataModel> readmeDBList = new ArrayList<>();
        HashSet<String> repositoryPathFromDBSet = new HashSet<>();
        try {
            Class.forName(Constant.DB_DRIVE_NAME);
            connection = DriverManager.getConnection(Constant.DB_URL, Constant.DB_UID, Constant.DB_PWD);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            return readmeDBList;
        }
        String MainTableFullName = "[" + Constant.DB_NAME + "].[dbo].[" + Constant.TABLE_NAME + "]";

        String dbQueryContentSql;
        String dbQueryContentStartSql = "SELECT TOP " +
                (Constant.INDEX_LINE_MAX - readmeDBList.size());
        String dbQueryContentColumnSql = MainTableFullName + ".repositoryPath," +
                MainTableFullName + ".downloadURL," +
                MainTableFullName + ".impressionCount," +
                MainTableFullName + ".clickCount," +
                MainTableFullName + ".repositoryContent," +
                MainTableFullName + ".topicsList," +
                MainTableFullName + ".readmeContent" +
                " FROM " +
                MainTableFullName+
                " WHERE ";
        String queryCondition = "";
        if (!repositoryPath.equals("")) {
            queryCondition += MainTableFullName + ".repositoryPath like '%" + repositoryPath + "%' and ";
        }
        if (!downloadURL.equals("")) {
            queryCondition += "downloadURL like '%" + downloadURL + "%' and ";
        }
        for (String topic : topicsList) {
            if (!topic.equals("")) {
                queryCondition += "topicsList like '%" + topic + "%' and ";
            }
        }
        repositoryContent = repositoryContent.replaceAll("\t", " ").trim();
        if (!repositoryContent.equals("")) {
            String[] repositoryContentSplits = repositoryContent.split(" ");
            try {
                for (int len = repositoryContentSplits.length; len > 0; len--) {
                    String queryConditionCur = queryCondition;
                    dbQueryContentStartSql = "SELECT TOP " +
                            (Constant.INDEX_LINE_MAX - readmeDBList.size());
                    String[] repositoryContentIndexList = new String[repositoryContentSplits.length - len + 1];
                    for (int index = 0; index < repositoryContentIndexList.length; index++) {
                        for (int cur = 0; cur < len; cur++) {
                            if (cur == 0) {
                                repositoryContentIndexList[index] = "";
                            } else {
                                repositoryContentIndexList[index] += " ";
                            }
                            repositoryContentIndexList[index] += repositoryContentSplits[index + cur];
                        }
                    }
                    queryConditionCur += "(";
                    for (String repositoryContentSplitElement : repositoryContentIndexList) {
                        queryConditionCur += "repositoryContent like '%" + repositoryContentSplitElement + "%' or ";
                    }
                    dbQueryContentSql = dbQueryContentStartSql + dbQueryContentColumnSql + queryConditionCur.substring(0, queryConditionCur.length() - 4) + ") ORDER BY " + Constant.RANK_DEFAULT_COLUMN_NAME;
                    Jdbc.ExecuteReaderByText(readmeDBList, repositoryPathFromDBSet, statement, dbQueryContentSql);
                    if (readmeDBList.size() >= Constant.INDEX_LINE_MAX) {
                        statement.close();
                        connection.close();
                        return readmeDBList;
                    }
                }
                statement.close();
                connection.close();
                return readmeDBList;

            } catch (Exception ex) {
                statement.close();
                connection.close();
//                Logger.WriteLog("get exist readme data error: " + ex.getMessage());
                return readmeDBList;
            }
        } else {
            dbQueryContentSql = dbQueryContentStartSql + dbQueryContentColumnSql + queryCondition.substring(0, queryCondition.length() - 5) + " ORDER BY " + Constant.RANK_DEFAULT_COLUMN_NAME;
            try {
                Jdbc.ExecuteReaderByText(readmeDBList, repositoryPathFromDBSet, statement, dbQueryContentSql);
                statement.close();
                connection.close();
                return readmeDBList;
            } catch (Exception ex) {
                statement.close();
                connection.close();
//                Logger.WriteLog("get exist readme data error: " + ex.getMessage());
                return readmeDBList;
            }
        }
    }

}
