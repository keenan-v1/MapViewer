package com.wurmly.mapviewer.localization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unused")
class Params
{
    String testcase;
    String expectedString;
    int expectedInt;
    Locale testLocale;

    Params(final String c, final String s, final Locale l)
    {
        testcase = c;
        expectedString = s;
        testLocale = l;
    }

    Params(final String c, final int i, final Locale l)
    {
        testcase = c;
        expectedInt = i;
        testLocale = l;
    }
}

class LocalizationTests
{
    private static Stream<Params> mnemonicParamProvider()
    {
        return Stream.of(
                new Params("test-a", KeyEvent.VK_A, Locale.getDefault()),
                new Params("test-b", KeyEvent.VK_B, Locale.getDefault()),
                new Params("test-c", KeyEvent.VK_AMPERSAND, Locale.getDefault()),
                new Params("test-a", KeyEvent.VK_S, Locale.GERMANY),
                new Params("test-b", KeyEvent.VK_T, Locale.GERMANY),
                new Params("test-c", KeyEvent.VK_AMPERSAND, Locale.GERMANY),
                new Params("test-c", KeyEvent.VK_AMPERSAND, Locale.JAPAN)
        );
    }

    private static Stream<Params> messageParamProvider()
    {
        return Stream.of(
                new Params("test-a", "Test Default", Locale.getDefault()),
                new Params("test-b", "Default Default", Locale.getDefault()),
                new Params("test-a", "Standard testen", Locale.GERMANY),
                new Params("test-b",  "Default Default", Locale.GERMANY),
                new Params("test-b",  "Default Default", Locale.JAPAN)
        );
    }

    private static Stream<Params> htmlParamProvider()
    {
        return Stream.of(
                new Params("html/about.html", "<html><div>Test Default</div></html>", Locale.getDefault()),
                new Params("html/about.html", "<html><div>Standard testen</div></html>", Locale.GERMANY),
                new Params("html/about.html",  "<html><div>Test Default</div></html>", Locale.JAPAN)
        );
    }

    @ParameterizedTest
    @MethodSource("mnemonicParamProvider")
    void getMnemonicForTest(final Params p)
    {
        Localization.getInstance().setLocale(p.testLocale);
        assertEquals(p.expectedInt, Localization.getInstance().getMnemonicFor(p.testcase));
    }

    @ParameterizedTest
    @MethodSource("messageParamProvider")
    void getMessageForTest(final Params p)
    {
        Localization.getInstance().setLocale(p.testLocale);
        assertEquals(p.expectedString, Localization.getInstance().getMessageFor(p.testcase));
    }

    @ParameterizedTest
    @MethodSource("htmlParamProvider")
    void getHtmlForTest(final Params p)
    {
        Localization.getInstance().setLocale(p.testLocale);
        assertEquals(p.expectedString, Localization.getInstance().getHtmlFor(p.testcase));
    }

    @Test
    void getTemplatedMessageForShouldThrowException()
    {
        assertThrows(IllegalArgumentException.class, () -> Localization.getInstance().getTemplatedMessageFor("test-a"));
    }

    @Test
    void getTemplatedMessageFor()
    {
        assertEquals("This is a test of tests", Localization.getInstance().getTemplatedMessageFor("test-template", "testcase", "test of tests"));
    }
}
