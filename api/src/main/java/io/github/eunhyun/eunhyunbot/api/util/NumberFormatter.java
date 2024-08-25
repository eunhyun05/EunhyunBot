package io.github.eunhyun.eunhyunbot.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberFormatter {

    public static String commas(int number) {
        return new DecimalFormat("###,###").format(number);
    }

    public static String commas(long number) {
        return new DecimalFormat("###,###").format(number);
    }
}