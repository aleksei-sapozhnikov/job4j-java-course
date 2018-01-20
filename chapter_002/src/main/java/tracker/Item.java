package tracker;

/**
 * Object to store in tracker.
 */
public class Item {

    /**
     * Unique Item identifier.
     */
    private String id;

    /**
     * Name.
     */
    private String name;

    /**
     * Detailed description.
     */
    private String description;

    /**
     * Time when the Item was created, in milliseconds.
     */
    private long createTime;

    /**
     * Comments left to this Item in tracker.
     */
    private String[] comments;

    /**
     * Get id.
     *
     * @return Id field value.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set id.
     *
     * @param id Value to set.
     */
    public void setId(String id) {
        this.id = id;
    }
}
