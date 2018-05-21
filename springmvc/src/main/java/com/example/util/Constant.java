package com.example.util;

public class Constant {
    public static final String DB_DRIVE_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String DB_URL = "jdbc:sqlserver://STCVM-H03:1433;DatabaseName=Crawler";
    public static final String DB_SERVER = "STCVM-H03";
    public static final String DB_NAME = "Crawler";
    public static final String DB_UID = "sa";
    public static final String DB_PWD = "123456Abcd";
    public static final String TABLE_NAME = "CrawlerGitDetailData";
    public static final int INDEX_LINE_MAX = 3000;
    public static final String RANK_DEFAULT_COLUMN_NAME = "-impressionCount,-clickCount,id";
    public static final int DOWNLOAD_WAIT_TIME_SECOND = 45;
    public static final String LOG_FILE_DIR = ".\\log";
}
