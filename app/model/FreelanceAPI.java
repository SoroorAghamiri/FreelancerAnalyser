package model;

public enum FreelanceAPI {
    BASE_URL("https://www.freelancer.com"),
    SEARCH_TERM("/api/projects/0.1/projects/active/?limit=10&query=");

    private String url;

    FreelanceAPI(String apiUrl) {
        this.url = apiUrl;
    }
 
    public String getUrl() {
        return url;
    }
  }
