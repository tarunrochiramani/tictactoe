package com.tr.utils;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class HelperTest {
    private Helper helper = new Helper();

    @Test
    public void canTokenizeEscapedUser() {
        List<String> strings = helper.tokenizeEscapedUser("<@U012ABCDEF|ernie09.c> don't wake me up at night anymore in <#C012ABCDE|here>");

        assertNotNull(strings);
        assertFalse(strings.isEmpty());
        assertEquals(1, strings.size());
        assertEquals("<@U012ABCDEF|ernie09.c>", strings.get(0));
        assertEquals("U012ABCDEF", helper.getUserId(strings.get(0)));
    }


    @Test
    public void canTokenizeEscapedChannel() {
        List<String> strings = helper.tokenizeEscapedChannel("<@U012ABCDEF|ernie> don't wake me up at night anymore in <#C012ABCDE|he_re>");

        assertNotNull(strings);
        assertFalse(strings.isEmpty());
        assertEquals(1, strings.size());
        assertEquals("<#C012ABCDE|he_re>", strings.get(0));
        assertEquals("C012ABCDE", helper.getChannelID(strings.get(0)));
    }

    @Test
    public void canValidateMoveText() {
        assertFalse(helper.validMoveText("ab cd"));
        assertFalse(helper.validMoveText("a1 c2"));
        assertFalse(helper.validMoveText("a12"));
        assertFalse(helper.validMoveText("a12 9"));
        assertFalse(helper.validMoveText("12 9"));
        assertTrue(helper.validMoveText("1 9"));
    }
}
