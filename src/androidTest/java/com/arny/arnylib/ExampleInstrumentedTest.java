package com.arny.arnylib;

import android.content.Context;

import com.arny.arnylib.security.CryptoStrings;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals("com.arny.vkmvp", appContext.getPackageName());
	}

    @Test
    public void base64() throws Exception {
        String input = "123456789";
        String encode = CryptoStrings.encryptBase64(input);
        System.out.println(encode);
        assertThat(encode).isNotNull();
        String decode = CryptoStrings.decryptBase64(encode);
        System.out.println(decode);
        assertThat(decode).isNotNull();
    }

}
