package model;
/**
 * <code>
 *     List of Portfolios
 * </code>
 * It displays 10 latest portfolios of employer
 * @author Bariq
 */
public class Portifolio {
    /**
     * Initialization of variables
     */
    String title;
    String Desc;
    /**
     * defining the default constructor so that if the user pass
     * empty class definition then it should handle
     */
    public Portifolio() {
    }
    /**
     *
     * @param title title
     * @param desc desc
     */
    public Portifolio(String title, String desc) {
        this.title = title;
        this.Desc = desc;
    }
    /**
     * Gets the title of portfolio
     * @return title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gets the description of the portfolio
     * @return desc
     */
    public String getDesc() {
        return Desc;
    }
    /**
     * @param desc
     */
    public void setDesc(String desc) {
        this.Desc = desc;
    }
}
