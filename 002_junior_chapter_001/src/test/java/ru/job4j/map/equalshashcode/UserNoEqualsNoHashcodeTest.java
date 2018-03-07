package ru.job4j.map.equalshashcode;

import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserNoEqualsNoHashcodeTest {

    /**
     * Test adding to Map.
     */
    @Test
    public void whenTwoUserWithTheSameFieldsThenTwoKeysInMap() {
        Map<AbstractUser, Object> map = new HashMap<>();
        AbstractUser user1 = new UserNoEqualsNoHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        AbstractUser user2 = new UserNoEqualsNoHashcode("Ivan", 12, LocalDate.of(1961, 4, 12));
        assertThat(map.put(user1, new Object()), is((Object) null));
        assertThat(map.put(user2, new Object()), is((Object) null));
        System.out.println(map);
    }
}