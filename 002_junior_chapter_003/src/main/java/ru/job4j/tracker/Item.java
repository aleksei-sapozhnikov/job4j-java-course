package ru.job4j.tracker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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
    private final String name;

    /**
     * Detailed description.
     */
    private final String description;

    /**
     * Time when the Item was created, in milliseconds.
     */
    private final long createTime;

    /**
     * Comments left to this Item in tracker.
     */
    private final String[] comments;


    public Item(String name, String description, long createTime) {
        this("-1", name, description, createTime, new String[0]);
    }

    public Item(String name, String description, long createTime, String[] comments) {
        this("-1", name, description, createTime, comments);
    }

    public Item(String id, String name, String description, long createTime, String[] comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.comments = comments;
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

    public String[] getComments() {
        return Arrays.copyOf(this.comments, this.comments.length);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.createTime), ZoneId.systemDefault()).format(formatter);
        return String.format("[id=%s, name=%s, description=%s, createTime=\'%s\', comments=%s]",
                this.id, this.name, this.description, time, Arrays.toString(this.comments));
    }

}
