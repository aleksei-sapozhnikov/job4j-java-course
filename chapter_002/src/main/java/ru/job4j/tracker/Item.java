package ru.job4j.tracker;

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


    public Item(String name, String description, long createTime) {
        this.name = name;
        this.description = description;
        this.createTime = createTime;
    }

    /**
     * Get id.
     *
     * @return Id field value.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get name.
     *
     * @return Name field value.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get detailed description.
     *
     * @return Description field value.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get time of creation.
     *
     * @return CreateTime field value.
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * Get comments of this Item.
     *
     * @return Comments field value.
     */
    public String[] getComments() {
        return comments;
    }


    /**
     * Set id.
     *
     * @param id Value to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set name.
     *
     * @param name Value to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set description.
     *
     * @param description Value to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set time of creation.
     *
     * @param createTime Value to set.
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * Set comments.
     *
     * @param comments Value to set.
     */
    public void setComments(String[] comments) {
        this.comments = comments;
    }

}
