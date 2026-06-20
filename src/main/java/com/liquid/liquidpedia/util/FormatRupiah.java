package com.liquid.liquidpedia.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatRupiah {

    public static String format(Double amount) {
        if (amount == null) return "Rp0";
        NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp" + format.format(amount);
    }
}
