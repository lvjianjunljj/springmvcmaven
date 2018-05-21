package com.example.model;

public class DBCrawlerGitDetailDataModel {
    private String repositoryPath;
    private String downloadURL;
    private int impressionCount;
    private int clickCount;
    private String repositoryContent;
    private String[] topicsList;
    private String readmeFileContent;
    private int index;

    public DBCrawlerGitDetailDataModel(
            String repositoryPath,
            String downloadURL,
            int impressionCount,
            int clickCount,
            String repositoryContent,
            String[] topicsList,
            String readmeFileContent) {
        this.repositoryPath = repositoryPath;
        this.downloadURL = downloadURL;
        this.impressionCount = impressionCount;
        this.clickCount = clickCount;
        this.repositoryContent = repositoryContent;
        this.topicsList = topicsList;
        this.readmeFileContent = readmeFileContent;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public int getImpressionCount() {
        return impressionCount;
    }

    public void setImpressionCount(int impressionCount) {
        this.impressionCount = impressionCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getRepositoryContent() {
        return repositoryContent;
    }

    public void setRepositoryContent(String repositoryContent) {
        this.repositoryContent = repositoryContent;
    }

    public String[] getTopicsList() {
        return topicsList;
    }

    public String getTopicsListContent() {
        String topicsListContent = "";
        for (String topic : this.topicsList) {
            topicsListContent += topic + "\t";
        }
        return topicsListContent;
    }

    public void setTopicsList(String[] topicsList) {
        this.topicsList = topicsList;
    }

    public String getReadmeFileContent() {
        return readmeFileContent;
    }

    public void setReadmeFileContent(String readmeFileContent) {
        this.readmeFileContent = readmeFileContent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
