package com.arny.arnylib;

import android.util.Log;
import com.arny.arnylib.utils.MathUtils;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LibMathUnitTest {
	@Test
	public void MathToExpo() throws Exception {
        String s = MathUtils.toExpo(14654654, 3);
        assertThat(s).isNotNull();

        String s2 = MathUtils.toExpo(0.00000554599, 3);
        assertThat(s2).isNotNull();
    }


}