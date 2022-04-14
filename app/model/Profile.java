package model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * It has all the functionality to create the model for the required data
 * All the data have the been used to display the user information
 *
 * @author Bariq
 */

import java.util.*;
public class Profile {
    /**
     *
     * initialization of the variables for the model
     *
     */
    String name;
    String city;
    String country;
    String role;
    boolean verified;
    String paymentMethod;
    int jobcount;
    int recommendedby;
    long registration_date;
    List<Portifolio> portifolios;

    /**
     * defining the default constructor so that if the user pass
     * empty class definition then it should handle
     */
    public Profile() {
    }

    /**
     *
     * @param name name
     * @param city city
     * @param country country
     * @param role role
     * @param verified is verified
     * @param paymentMethod payment method
     * @param jobcount jobcount
     * @param recommendedby recommended by
     * @param registration_date data of registration
     * @param portifolios portflio
     * all are the constructor initalization of the variable
     */
    public Profile(String name, String city, String country, String role, boolean verified, String paymentMethod, int jobcount, int recommendedby, long registration_date, List<Portifolio> portifolios) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.role = role;
        this.verified = verified;
        this.paymentMethod = paymentMethod;
        this.jobcount = jobcount;
        this.recommendedby = recommendedby;
        this.registration_date = registration_date;
        this.portifolios = portifolios;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * to set the name on the variable of the class
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * similar getter and setter
     */

    public long getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(long registration_date) {
        this.registration_date = registration_date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getJobcount() {
        return jobcount;
    }

    public void setJobcount(int jobcount) {
        this.jobcount = jobcount;
    }

    public int getRecommendedby() {
        return recommendedby;
    }

    public void setRecommendedby(int recommendedby) {
        this.recommendedby = recommendedby;
    }

    public List<Portifolio> getPortifolios() {
        return portifolios;
    }

    public void setPortifolios(List<Portifolio> portifolios) {
        this.portifolios = portifolios;
    }
    public Profile CreateProfile(JsonNode jsonNode,String userId){

        JsonNode user = jsonNode.get("result").get("users").get(userId);
        ArrayNode portfolios= (ArrayNode) jsonNode.get("result").get("portfolios").get(userId);
        List<Portifolio> portifolioList = new ArrayList<>();
        if(portfolios!=null){
            for(JsonNode portfolio: portfolios){
                portifolioList.add(new Portifolio(portfolio.get("title").toPrettyString(),portfolio.get("description").toPrettyString()));
            }
        }
        String name= user.get("username").toPrettyString();

        ArrayNode jobs =(ArrayNode) user.get("jobs");
        int jobcount=jobs.size();
        String city = user.get("location").get("city").toPrettyString();
        String country = user.get("location").get("country").get("name").toPrettyString();
        long date= user.get("registration_date").asLong();
        String role = user.get("role").toPrettyString();
        String paymentMethod=user.get("primary_currency").get("code").toPrettyString()+user.get("primary_currency").get("sign").toPrettyString();
        boolean verified = user.get("status").get("email_verified").asBoolean();
        int recommendedby =user.get("recommendations").asInt();
        Profile profile = new Profile(name,city,country,role,verified,paymentMethod,jobcount,recommendedby,date,portifolioList);


        return profile;
    }
    public JsonNode generateJson(Profile profile){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(profile);
        return jsonNode;
    }
}
