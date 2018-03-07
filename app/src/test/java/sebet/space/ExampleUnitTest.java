package sebet.space;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import sebet.space.decoding.FormatDecoder;
import sebet.space.decoding.XmlComposer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() throws Exception {
        /*FormatDecoder decoder = new FormatDecoder();
        decoder.decode("%1/Fluorouracil%5/892.3/520.5");
        decoder.decode("132.145.231.23");
        /*for (Iterator<Map.Entry<String, String>> iterator = decoder.getData().entrySet().iterator(); iterator.hasNext();){
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        System.out.println(XmlComposer.XmlAsString(decoder));*/
        int x = 100;
        int y = 25;
    }
}