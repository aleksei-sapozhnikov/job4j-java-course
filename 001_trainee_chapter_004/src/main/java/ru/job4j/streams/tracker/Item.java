package ru.job4j.streams.tracker;

/**
 * Object to store in tracker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.01.2018
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
     * Set id.
     *
     * @param id Value to set.
     */
    public void setId(String id) {
        this.id = id;
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
     * Set name.
     *
     * @param name Value to set.
     */
    public void setName(String name) {
        this.name = name;
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

}
