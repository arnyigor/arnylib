package com.arny.arnylib;

import com.arny.arnylib.models.SMS;
import com.arny.arnylib.utils.DateTimeUtils;
import com.arny.arnylib.utils.Utility;
import com.arny.arnylib.utils.UtilsKt;
import org.junit.Test;

import java.util.ArrayList;

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

    @Test
    public void transliterate() throws Exception {
        String transliterate = UtilsKt.transliterate("игорь Седой юрьевич",true);
        System.out.println(transliterate);
        assertThat(transliterate).isNotNull();
    }



    @Test
    public void findPosition() throws Exception {
        String[] strings = {"f", "b", "1"};
        int position = UtilsKt.findPosition(strings, "1");
        System.out.println("position:" + position);
        assertThat(position).isGreaterThan(0);
        SMS sms1 = new SMS();
        sms1.set_id("123");
        SMS sms2 = new SMS();
        sms2.set_id("124");

        ArrayList<SMS> smss = new ArrayList<>();
        smss.add(sms1);
        smss.add(sms2);

        SMS test = new SMS();
        test.set_id("123");

        int position2 = UtilsKt.findPosition(smss, test);
        System.out.println("position2:" + position2);
        assertThat(0).isNotNegative();
        assertThat(position2).isNotNegative();
        assertThat(position2).isNotEqualTo(-1);
    }


}