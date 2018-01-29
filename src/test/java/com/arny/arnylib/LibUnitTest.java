package com.arny.arnylib;

import com.arny.arnylib.utils.DateTimeUtils;
import com.arny.arnylib.utils.MathUtils;
import com.arny.arnylib.utils.Utility;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LibUnitTest {
	@Test
    public void DateFormat() throws Exception {
        String strDate = "22-февр.-2007";
        strDate = strDate.replace("-", " ").replace(".", " ").replaceAll("\\s+", " ");
        String format = DateTimeUtils.dateFormatChooser(strDate);
        format = "dd MMMM yyyy";
        long time = DateTimeUtils.convertTimeStringToLong(strDate, format);
        assertThat(time).isPositive();
        String dateTime = DateTimeUtils.getDateTime(time, "dd MM yyyy");
        System.out.println(dateTime);
    }

    @Test
    public void empty() throws Exception {
        String str = "\r\n";
        boolean empty = Utility.empty(str);
        assertThat(empty).isTrue();
    }


}