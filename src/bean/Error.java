package bean;

/**
 * Created by lookitsmarc on 08/04/2017.
 */
public class Error {

    // Private field for the error bean
    private String error;
    private String level;

    /**
     * Set Error
     * @param error
     * @public
     */
    public void setError(String error){
        this.error = error;
    }

    /**
     * Set Level
     * @param level
     * @public
     */
    public void setLevel(String level){
        this.level = level;
    }

    /**
     * Get Level
     * @return {String} level
     * @public
     */
    public String getLevel(){
        return this.level;
    }

    /**
     * Get Error
     * @return {String} error
     * @public
     */
    public String getError(){
        return this.error;
    }
}
