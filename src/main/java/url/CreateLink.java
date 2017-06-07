package url;

import helper.Loghandler;

/**
 * Created by Marc Intha-amnouay, Didier Youn and Antoine Renault on 07/06/2017.
 */
public class CreateLink {

    // private variable
    private long hash;
    private int sqlrow;

    // Final variable
    private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Constructor
     */
    public CreateLink(int sqlRow) {
        this.sqlrow = sqlRow;
    }

    /**
     *
     * @param uniqID
     * @return
     */
    public String base10ToBase62(long uniqID){
        StringBuilder sb = new StringBuilder();

        if (uniqID == 0)
            return "a";

        while (uniqID > 0)
            uniqID = generateBase62(sb, uniqID);

        return sb.reverse().toString();
    }

    /**
     *
     * @param sb
     * @param currentNumb
     * @return
     */
    protected long generateBase62(final StringBuilder sb, long currentNumb){
        long remaining = currentNumb % ALPHABET.length();
        sb.append(ALPHABET.charAt((int) remaining));

        return currentNumb / ALPHABET.length();
    }

    /**
     * Finalize Short URL
     *          Finalize the creation of the Hash and the UrlAPI and push it into the Datbase
     * @return
     */
    public String encodeLongURL(){
        long rand = 1 + (long) ((Math.random() * (Math.pow(62, 6) - 1)));

        // Now we need to concat the value of the random number with the value of the row
        this.hash = concatLong(this.sqlrow, rand);
        Loghandler.log("random value "+String.valueOf(this.hash), "info");

        String base62 = this.base10ToBase62(this.hash);

        Loghandler.log(base62, "info");
        return base62;
    }

    /**
     *
     * @param row
     * @param rand
     * @return
     */
    public Long concatLong(int row, long rand) {
        long concat = Long.valueOf(String.valueOf(rand) + String.valueOf(row));

        return concat;
    }

    /**
     * Get Short URL Hash
     * @return
     */
    public Long getShortURLHash() {
        return this.hash;
    }
}
