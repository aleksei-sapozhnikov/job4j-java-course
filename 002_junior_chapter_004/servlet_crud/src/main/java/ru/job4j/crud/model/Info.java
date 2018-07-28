package ru.job4j.crud.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static ru.job4j.crud.model.Info.Fields.*;

/**
 * Holds information and lets "get" to it by getters.
 * Like there are a lot of fields inside.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class Info {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Info.class);
    /**
     * Inner storage - field holding info values.
     */
    private final Map<Fields, String> infoValues;

    /**
     * Constructs new object using array of strings in order.
     * Order is: NAME, EMAIL, COUNTRY, CITY.
     *
     * @param values Array of values.
     */
    public Info(String... values) {
        if (values.length < Fields.values().length) {
            LOG.error("Given array of values has less length then needed");
        }
        this.infoValues = Collections.unmodifiableMap(this.fillValues(Arrays.asList(values)));
    }

    /**
     * Constructs new object using given map of values.
     *
     * @param valuesMap Map with values.
     */
    public Info(Map<Fields, String> valuesMap) {
        if (valuesMap.values().size() < Fields.values().length) {
            LOG.error("Given map of values has less values then needed");
        }
        this.infoValues = Collections.unmodifiableMap(valuesMap);
    }

    /**
     * Returns name.
     *
     * @return Name.
     */
    public String getName() {
        return this.infoValues.get(NAME);
    }

    /**
     * Returns email.
     *
     * @return Email.
     */
    public String getEmail() {
        return this.infoValues.get(EMAIL);
    }

    /**
     * Returns country.
     *
     * @return Country.
     */
    public String getCountry() {
        return this.infoValues.get(COUNTRY);
    }

    /**
     * Returns city.
     *
     * @return City.
     */
    public String getCity() {
        return this.infoValues.get(CITY);
    }

    /**
     * Fills values into the inner storage map.
     *
     * @param values List of values. Order: NAME, EMAIL, COUNTRY, CITY.
     * @return Map with values.
     */
    private Map<Fields, String> fillValues(List<String> values) {
        Iterator<String> valuesIt = values.iterator();
        Map<Fields, String> result = new HashMap<>();
        result.put(NAME, valuesIt.next());
        result.put(EMAIL, valuesIt.next());
        result.put(COUNTRY, valuesIt.next());
        result.put(CITY, valuesIt.next());
        return result;
    }

    /**
     * Merges this object with newer one. Checks every field. If the field in newer
     * object is not null, then replaces older value from the newer object.
     * Else - leaves the value from the older object.
     *
     * @param newer Newer object.
     * @return Updated object.
     */
    public Info mergeWith(Info newer) {
        Map<Fields, String> result = new HashMap<>();
        for (Fields field : Fields.values()) {
            result.put(field,
                    newer.infoValues.get(field) != null
                            ? newer.infoValues.get(field)
                            : this.infoValues.get(field)
            );
        }
        return new Info(result);
    }

    /**
     * Returns string representing the object.
     *
     * @return String representing the object.
     */
    @Override
    public String toString() {
        return String.format(
                "[info name=%s, email=%s, country=%s, city=%s]",
                this.infoValues.get(NAME),
                this.infoValues.get(EMAIL),
                this.infoValues.get(COUNTRY),
                this.infoValues.get(CITY)
        );
    }

    /**
     * Check equality of this object and other object.
     *
     * @param o Other object.
     * @return <tt>true</tt> if objects are equal, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Info that = (Info) o;
        return Objects.equals(this.infoValues, that.infoValues);
    }

    /**
     * Returns integer hashcode of the object.
     *
     * @return Hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.infoValues);
    }

    /**
     * Class of possible fields to hold in the Info class objects.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version $Id$
     * @since 0.1
     */
    public enum Fields {
        NAME,
        EMAIL,
        COUNTRY,
        CITY
    }
}
    