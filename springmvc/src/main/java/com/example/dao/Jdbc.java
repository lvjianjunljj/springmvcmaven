package com.example.dao;

import com.example.model.DBCrawlerGitDetailDataModel;
import com.example.util.Constant;

import java.sql.*;
import java.util.HashSet;
import java.util.List;

public class Jdbc {
    static Connection dbConn;

    public String testConn() {

        try {
            Class.forName(Constant.DB_DRIVE_NAME);
            dbConn = DriverManager.getConnection(Constant.DB_URL, Constant.DB_UID,Constant.DB_PWD);
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public int getData() {
        Connection ct = null;
        Statement sm = null;
        ResultSet rs = null;
        try {
            //1、加载驱动(把需要的驱动程序加入内存)
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Class.forName(Constant.DB_DRIVE_NAME);

            //2、得到连接(指定连接到哪个数据源、数据库的用户名和密码)
            //如果配置数据源的时候选择的是windows NT验证方式，则不需要数据库的用户名和密码
//            ct=DriverManager.getConnection("jdbc:odbc:ywq");
            ct = DriverManager.getConnection(Constant.DB_URL, Constant.DB_UID,Constant.DB_PWD);

            //3、创建Statement或者PreparedStatement(区别)
            //Statement用处：主要用于发送SQL语句到数据库
            sm = ct.createStatement();

            //4、执行(CRUD，创建数据库、备份数据库、删除数据库)
            //演示1：添加一条数据到dept表中
            //executeUpdate可以执行CUD操作(添加、删除、修改)
//          int i=sm.executeUpdate("insert into people values(25,'张三','男')");
//          int q=sm.executeUpdate("insert into people values(20,'李四','男')");
//          int z=sm.executeUpdate("insert into people values(30,'小三','女')");
//          if(i==1&&q==1&&z==1){
//              System.out.println("数据添加成功");
//          }else{
//              System.out.println("添加失败");
//          }

            //演示2：从dept表中删除一条记录
//          int j=sm.executeUpdate("delete from people where age=25");
//          System.out.println(j);
//          //此处j的值为本次删除的记录条数
//          if(j==1){
//              System.out.println("数据删除成功");
//          }else{
//              System.out.println("删除失败");
//          }

            //演示3：从people表中修改age=20 name改为李四他爹
//          int k=sm.executeUpdate("update people set name='李四他爹' where age='20'");
//          if(k==1){
//              System.out.println("数据修改成功");
//          }else{
//              System.out.println("修改失败");
//          }

            //演示4：查询,显示所有的部门信息
            //ResultSet结果集,大家可以把ResultSet理解成返回一张表行的结果集
            int a = 0;
            rs = sm.executeQuery("SELECT count(*) as 'count'" +
                    "    FROM [Crawler].[dbo].[CrawlerGitDetailData]" +
                    " WHERE  " +
                    "[Crawler].[dbo].[CrawlerGitDetailData].repositoryPath like '%https://github.com%'");
            //循环取出
            while (rs.next()) {
                a = rs.getInt(1);
            }
            return a;
        } catch (Exception e) {
            return -1;
        } finally {
            //关闭资源，关闭顺序:先创建后关闭，后创建先关闭
            try {
                //为了程序健壮
                if (rs != null) {
                    rs.close();
                }
                if (sm != null) {
                    sm.close();
                }
                if (ct != null) {
                    ct.close();
                }
//                return -2;
            } catch (SQLException e) {
//                return -3;
            }
        }
    }
    public static void ExecuteReaderByText(List<DBCrawlerGitDetailDataModel> readmeDBList, HashSet<String> repositoryPathFromDBSet, Statement statement, String dbQueryContentSql) throws SQLException {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(dbQueryContentSql);
            while (resultSet.next()) {
                String repositoryPathFromDB = resultSet.getString("repositoryPath");
                if (repositoryPathFromDBSet.contains(repositoryPathFromDB)) {
                    continue;
                }
                String downloadURLFromDB = resultSet.getString("downloadURL");
                int impressionCountFromDB = resultSet.getInt("impressionCount");
                int clickCountFromDB = resultSet.getInt("clickCount");
                String repositoryContentFromDB = resultSet.getString("repositoryContent");
                String[] topicsListFromDB = resultSet.getString("topicsList").split(";");
                String readmeFileContent = resultSet.getString("readmeContent");
                DBCrawlerGitDetailDataModel model = new DBCrawlerGitDetailDataModel(repositoryPathFromDB,
                        downloadURLFromDB,
                        impressionCountFromDB,
                        clickCountFromDB,
                        repositoryContentFromDB,
                        topicsListFromDB,
                        readmeFileContent);
                readmeDBList.add(model);
                repositoryPathFromDBSet.add(repositoryPathFromDB);
                if (readmeDBList.size() >= Constant.INDEX_LINE_MAX) {
                    resultSet.close();
                    return;
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            resultSet.close();
        }
    }
}
