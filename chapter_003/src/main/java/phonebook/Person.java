package phonebook;

/**
 * Person for a phone book.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class Person {

    /**
     * Name.
     */
    private String name;

    /**
     * Surname.
     */
    private String surname;

    /**
     * Phone number.
     */
    private String phone;

    /**
     * Address, place of living.
     */
    private String address;

    /**
     * Constructor.
     *
     * @param name    name.
     * @param surname surname.
     * @param phone   phone number.
     * @param address address, place of living.
     */
    Person(String name, String surname, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Give information about current user.
     *
     * @return string with this user field values.
     */
    String info() {
        return String.format("%s, %s, %s, %s", this.name, this.surname, this.phone, this.address);
    }
}