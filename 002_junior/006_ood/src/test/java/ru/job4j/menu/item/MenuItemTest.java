package ru.job4j.menu.item;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MenuItemTest {

    @Test
    public void testGetters() {
        var action = Mockito.mock(ActionAble.class);
        var root = new MenuItem("root", "root_name", null, action);
        assertThat(root.getId(), is("root"));
        assertThat(root.getName(), is("root_name"));
        assertThat(root.getChildren(), is(Collections.emptyList()));
        assertNull(root.getParent());
        //
        var sub = new MenuItem("sub", "sub_name", root, action);
        assertThat(sub.getId(), is("sub"));
        assertThat(sub.getName(), is("sub_name"));
        assertThat(sub.getChildren(), is(Collections.emptyList()));
        assertThat(sub.getParent(), is(root));
    }

    @Test
    public void whenAddChildThenTheSubObjectReturnedInGetChildren() {
        var action = Mockito.mock(ActionAble.class);
        var root = new MenuItem("root", "root_name", null, action);
        var sub = new MenuItem("sub", "sub_name", root, action);
        assertThat(root.getChildren(), is(Collections.emptyList()));
        root.addChild(sub);
        assertThat(root.getChildren(), is(List.of(sub)));
        assertSame(root.getChildren().get(0), sub);
    }

    @Test
    public void whenActionCalledThenActionObjectCalled() {
        var action = Mockito.mock(ActionAble.class);
        var wasCalled = new boolean[]{false};
        Mockito.doAnswer(i -> wasCalled[0] = true).when(action).doAction();
        // root node
        var root = new MenuItem("root", "root_name", null, action);
        root.doAction();
        assertTrue(wasCalled[0]);
        // sub node
        var sub = new MenuItem("sub", "sub_name", root, action);
        wasCalled[0] = false; // reset
        sub.doAction();
        assertTrue(wasCalled[0]);
    }

    @Test
    public void testEqualsAndHashCode() {
        var action = Mockito.mock(ActionAble.class);
        var root = new MenuItem("root", "root_name", null, action);
        //
        var main = new MenuItem("main", "main_name", null, action);
        var otherIdRoot = new MenuItem("other", "main_name", null, action);
        var otherIdSub = new MenuItem("other", "main_name", root, action);
        var sameId = new MenuItem("main", "other_name", root, action);
        //
        assertNotEquals(main, "Other class");
        assertNotEquals(main, null);
        assertNotEquals(main, otherIdRoot);
        assertNotEquals(main, otherIdSub);
        assertEquals(main, sameId);
        //
        assertEquals(main.hashCode(), sameId.hashCode());
    }
}