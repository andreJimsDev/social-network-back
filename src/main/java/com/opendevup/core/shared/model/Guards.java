package com.opendevup.core.shared.model;

import com.opendevup.core.shared.model.exceptions.ValidationInputException;
import org.apache.commons.lang3.StringUtils;

public final class Guards {

    public static void notEmpty(String input, String exceptionMessage) {
        if (StringUtils.isBlank(input)) {
            throw new ValidationInputException(exceptionMessage);
        }
    }

    public static <T> void notNull(T input, String exceptionMessage) {
        if (input == null) {
            throw new ValidationInputException(exceptionMessage);
        }
    }
}
