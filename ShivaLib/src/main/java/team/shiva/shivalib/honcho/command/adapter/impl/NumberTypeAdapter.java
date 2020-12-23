package team.shiva.shivalib.honcho.command.adapter.impl;

import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import java.text.NumberFormat;
import java.text.ParseException;
import org.bukkit.Bukkit;

public class NumberTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(String string, Class<T> type) {
        try {
            Number number = NumberFormat.getNumberInstance().parse(string);
            if(type.equals(byte.class) || type.equals(Byte.class)){
                return type.cast(number.byteValue());
            }
            if(type.equals(double.class) || type.equals(Double.class)){
                return type.cast(number.doubleValue());
            }
            if(type.equals(float.class) || type.equals(Float.class)){
                return type.cast(number.floatValue());
            }
            if(type.equals(int.class) || type.equals(Integer.class)){
                return type.cast(number.intValue());
            }
            if(type.equals(long.class) || type.equals(Long.class)){
                return type.cast(number.longValue());
            }
            if(type.equals(short.class) || type.equals(Short.class)){
                return type.cast(number.shortValue());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
