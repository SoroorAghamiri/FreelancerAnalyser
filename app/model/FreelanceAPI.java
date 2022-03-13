package model;

public enum FreelanceAPI {
    BASE_URL("https://www.freelancer.com"),
    SEARCH_TERM("/api/projects/0.1/projects/active/?limit=10&job_details=true&full_description=true&query="),
    OWNER_PROFILE("/api/users/0.1/users/"),
    WORD_STATS("/api/projects/0.1/projects/active/?limit=250&job_details=true&full_description=true&query=");

    private String url;
    
    FreelanceAPI(String apiUrl) {
        this.url = apiUrl;
    }
 
    public String getUrl() {
        return url;
    }
  }
