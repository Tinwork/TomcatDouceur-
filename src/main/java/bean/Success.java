package bean;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 07/06/2017.
 */
public class Success {

    private String message;
    private String status;

    /**
     *
     */
    public Success(){}

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
