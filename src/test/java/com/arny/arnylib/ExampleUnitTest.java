package com.arny.arnylib;

import com.arny.arnylib.utils.Params;
import com.arny.arnylib.utils.Utility;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void emails_isCorrect() throws Exception {
        List<String> list = Arrays.asList("","4844","@","a@m.r","a@.ru");
        for (String s : list) {
            assertTrue(!Utility.isEmailValid(s));
        }
    }

    @Test
    public void test_is_empty() throws Exception {
        assertTrue(Utility.empty(""));
        assertTrue(Utility.empty(null));
        assertTrue(Utility.empty("null"));
    }

    @Test
    public void params() throws Exception {
        Params params = new Params("{}");
        assertThat(!Utility.empty(params));
        assertThat(params.toString()).isNull();
        Params params1 = new Params("param1=1");
        assertThat(params1).isNotNull();
    }
}