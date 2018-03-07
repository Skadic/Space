package sebet.space.decoding;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import sebet.space.utils.CRC16;
import sebet.space.utils.EnumTasks;

/**
 * Created by eti22 on 17.08.2017.
 */

public class FormatDecoder {

    final static String
            DRUG_NAME = "0DrugName",
            INFUSION_RATE = "1InfusionRate",
            RATE_UNIT = "2RateUnit",
            VTBI_VALUE = "3VtbiValue",
            VTBI_UNIT = "4VtbiUnit",
            CHECKSUM = "5Checksum",
            IP = "6IP";

    private final SortedMap<String, String> data;

    public FormatDecoder() {
        data = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        data.put(RATE_UNIT, "ml/h");
        data.put(VTBI_UNIT, "ml");
    }

    public void decode(String code){
        switch (getTaskForCode(code)){
            case DRUG_CONTAINER: {
                String[] codes = code.split("%");

                codes = Arrays.copyOfRange(codes, 1, codes.length);

                for (String s : codes)
                    decodeToken(s.split("/"));
                break;
            }
            case PUMP: {
                data.put(IP, code);
                break;
            }
        }
        if(hasDrug() && hasPump()){
            calculateChecksum();
        }
    }

    private void decodeToken(String[] token){
        switch (token[0]) {
            case "1": {
                data.put(DRUG_NAME, token[1]);
                break;
            }
            case "5": {
                double rate = Double.parseDouble(token[1]);
                if(inRange(rate, 0.1D, 999.9D))
                    data.put(INFUSION_RATE, token[1]);

                double vtbi = Double.parseDouble(token[2]);
                if(inRange(vtbi, 0.1D, 9999.9D))
                    data.put(VTBI_VALUE, token[2]);

                break;
            }
        }
    }

    public EnumTasks getTaskForCode(String code){
        if(code.startsWith("%")) {
            return EnumTasks.DRUG_CONTAINER;
        } else if(stringContainsOnly(code, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.') && code.contains(".") && inRange(code.length(), 7, 15)){
            return EnumTasks.PUMP;
        }
        return null;
    }

    public long calculateChecksum(){
        /*data.put(CHECKSUM, String.valueOf(CRC16.getCRC("", "", data.get(DRUG_NAME), data.get(INFUSION_RATE), data.get(RATE_UNIT))));
         */
        StringBuilder sb = new StringBuilder();
        sb.append(data.get(DRUG_NAME));
        sb.append(data.get(INFUSION_RATE));
        sb.append(data.get(RATE_UNIT));
        sb.append(data.get(VTBI_VALUE));
        sb.append(data.get(VTBI_UNIT));

        data.put(CHECKSUM, String.valueOf(CRC16.calculate(sb.toString())));
        return Long.parseLong(data.get(CHECKSUM));
    }

    public boolean hasDrug(){
        return data.containsKey(VTBI_VALUE) && data.containsKey(INFUSION_RATE) && data.containsKey(DRUG_NAME);
    }

    public boolean hasPump(){
        return data.containsKey(IP);
    }

    public void reset(){
        data.clear();

        data.put(RATE_UNIT, "ml/h");
        data.put(VTBI_UNIT, "ml");
    }

    //---------------- Helper Methods -------------------------

    private boolean inRange(double d, double min, double max){
        return d >= min && d <= max;
    }

    private boolean stringContainsOnly(String s, Character... tokens){
        List<Character> tokenList = Arrays.asList(tokens);
        for (char c : s.toCharArray()) {
            if(!tokenList.contains(c))return false;
        }
        return true;
    }

    //------------------ Getter ----------------------

    public Map<String, String> getData() {
        return Collections.unmodifiableMap(data);
    }

    public String getIP(){
        if(hasPump())
            return data.get(IP);
        else
            return null;
    }
}
