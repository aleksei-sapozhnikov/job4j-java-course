package ru.job4j.crud.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static ru.job4j.crud.model.Info.Fields.*;

public class Info {

    private static final Logger LOG = LogManager.getLogger(Info.class);
    private final Map<Fields, String> infoValues;

    public Info(String... values) {
        if (values.length < Fields.values().length) {
            LOG.error("Given array of values has less length then needed");
        }
        this.infoValues = this.fillValues(Arrays.asList(values));
    }

    public Info(Map<Fields, String> valuesMap) {
        if (valuesMap.values().size() < Fields.values().length) {
            LOG.error("Given map of values has less values then needed");
        }
        this.infoValues = Collections.unmodifiableMap(valuesMap);
    }

    public String getName() {
        return this.infoValues.get(NAME);
    }

    public String getEmail() {
        return this.infoValues.get(EMAIL);
    }

    public String getCountry() {
        return this.infoValues.get(COUNTRY);
    }

    public String getCity() {
        return this.infoValues.get(CITY);
    }

    private Map<Fields, String> fillValues(List<String> values) {
        Iterator<String> valuesIt = values.iterator();
        Map<Fields, String> result = new HashMap<>();
        result.put(NAME, valuesIt.next());
        result.put(EMAIL, valuesIt.next());
        result.put(COUNTRY, valuesIt.next());
        result.put(CITY, valuesIt.next());
        return Collections.unmodifiableMap(result);
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(this.infoValues);
    }

    public enum Fields {
        NAME,
        EMAIL,
        COUNTRY,
        CITY
    }
}
    