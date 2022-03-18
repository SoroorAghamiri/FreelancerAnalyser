package model;

public enum FreelanceAPI {
    BASE_URL("https://www.freelancer.com"),
    SEARCH_TERM("/api/projects/0.1/projects/active/?limit=10&job_details=true&full_description=true&query="),
    OWNER_PROFILE("/api/users/0.1/portfolios/?limit=10&compact=true&portfolio_details=true&user_details=true&user_qualification_details=true&user_jobs=true&user_portfolio_details=true&user_recommendations=true&count=true&user_profile_description=true&users[]="),
    WORD_STATS("/api/projects/0.1/projects/active/?limit=250&job_details=true&full_description=true&query="),
    PROJECT_BY_ID("/api/projects/0.1/projects/");

    private String url;
    
    FreelanceAPI(String apiUrl) {
        this.url = apiUrl;
    }
 
    public String getUrl() {
        return url;
    }
  }
