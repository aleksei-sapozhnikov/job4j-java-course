package ru.job4j.crud.model;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.crud.model.Info.Fields.*;

public class InfoTest {
    /**
     * Info to test.
     */
    private final Info info = new Info("name", "email@mail.com", "country", "city");
    private final Info newInfo = new Info("new_name", "new_email@mail.com", "new_country", "new city");
    /**
     * Info with null values.
     */
    private final Info infoNameNull = new Info(null, "new_email@mail.com", "new_country", "new city");
    private final Info infoEmailNull = new Info("new_name", null, "new_country", "new city");
    private final Info infoCountryNull = new Info("new_name", "new_email@mail.com", null, "new city");
    private final Info infoCityNull = new Info("new_name", "new_email@mail.com", "new_country", null);

    /**
     * Test getters.
     */
    @Test
    public void whenGetterThenValue() {
        assertThat(this.info.getName(), is("name"));
        assertThat(this.info.getEmail(), is("email@mail.com"));
        assertThat(this.info.getCountry(), is("country"));
        assertThat(this.info.getCity(), is("city"));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        String expected = String.format(
                "[info name=%s, email=%s, country=%s, city=%s]",
                this.info.getName(),
                this.info.getEmail(),
                this.info.getCountry(),
                this.info.getCity()
        );
        assertThat(this.info.toString(), is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        Info main = this.info;
        // objects to compare
        Info itself = this.info;
        Info same = new Info(
                new HashMap<Info.Fields, String>() {
                    {
                        put(NAME, "name");
                        put(EMAIL, "email@mail.com");
                        put(COUNTRY, "country");
                        put(CITY, "city");
                    }
                });
        Info nameOther = new Info("other_name", "email@mail.com", "country", "city");
        Info emailOther = new Info("name", "other_email@mail.com", "country", "city");
        Info countryOther = new Info("name", "email@mail.com", "other_country", "city");
        Info cityOther = new Info("name", "email@mail.com", "country", "other_city");
        Info nullObject = null;
        String classOther = "I'm Info!";
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        // hashcode of equal objects
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        // not equal
        assertThat(main.equals(nameOther), is(false));
        assertThat(main.equals(emailOther), is(false));
        assertThat(main.equals(countryOther), is(false));
        assertThat(main.equals(cityOther), is(false));
        assertThat(main.equals(nullObject), is(false));
        assertThat(main.equals(classOther), is(false));
    }

    /**
     * Test mergeWith()
     */
    @Test
    public void whenMergeWithNewerThenFieldsUpdate() {
        Info result = this.info.mergeWith(this.newInfo);
        assertThat(result.getName(), is(this.newInfo.getName()));
        assertThat(result.getEmail(), is(this.newInfo.getEmail()));
        assertThat(result.getCountry(), is(this.newInfo.getCountry()));
        assertThat(result.getCity(), is(this.newInfo.getCity()));
    }

    @Test
    public void whenNewerHasNullValueThenMergeRemainsOldValue() {
        // new name null
        Info resultName = this.info.mergeWith(this.infoNameNull);
        assertThat(resultName.getName(), is(this.info.getName()));
        assertThat(resultName.getEmail(), is(this.infoNameNull.getEmail()));
        assertThat(resultName.getCountry(), is(this.infoNameNull.getCountry()));
        assertThat(resultName.getCity(), is(this.infoNameNull.getCity()));
        // new email null
        Info resultEmail = this.info.mergeWith(this.infoEmailNull);
        assertThat(resultEmail.getName(), is(this.infoEmailNull.getName()));
        assertThat(resultEmail.getEmail(), is(this.info.getEmail()));
        assertThat(resultEmail.getCountry(), is(this.infoEmailNull.getCountry()));
        assertThat(resultEmail.getCity(), is(this.infoEmailNull.getCity()));
        // new country null
        Info resultCountry = this.info.mergeWith(this.infoCountryNull);
        assertThat(resultCountry.getName(), is(this.infoCountryNull.getName()));
        assertThat(resultCountry.getEmail(), is(this.infoCountryNull.getEmail()));
        assertThat(resultCountry.getCountry(), is(this.info.getCountry()));
        assertThat(resultCountry.getCity(), is(this.infoCountryNull.getCity()));
        // new city null
        Info resultCity = this.info.mergeWith(this.infoCityNull);
        assertThat(resultCity.getName(), is(this.infoCityNull.getName()));
        assertThat(resultCity.getEmail(), is(this.infoCityNull.getEmail()));
        assertThat(resultCity.getCountry(), is(this.infoCityNull.getCountry()));
        assertThat(resultCity.getCity(), is(this.info.getCity()));
    }

}