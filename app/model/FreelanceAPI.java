package model;

public enum FreelanceAPI {
    SEARCH_TERM("https://www.freelancer.com/api/projects/0.1/projects/active/?limit=10&query=");

    private String url;

    FreelanceAPI(String apiUrl) {
        this.url = apiUrl;
    }
 
    public String getUrl() {
        return url;
    }
  }
