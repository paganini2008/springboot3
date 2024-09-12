package com.fred.common.utils;

import static com.fred.common.utils.GenericConverterFactorys.DATE_TO_LOCAL_DATE;
import static com.fred.common.utils.GenericConverterFactorys.DATE_TO_LOCAL_DATE_TIME;
import static com.fred.common.utils.GenericConverterFactorys.DATE_TO_LOCAL_TIME;
import static com.fred.common.utils.GenericConverterFactorys.LOCAL_DATE_TIME_TO_DATE;
import static com.fred.common.utils.GenericConverterFactorys.LOCAL_DATE_TIME_TO_LONG;
import static com.fred.common.utils.GenericConverterFactorys.LOCAL_DATE_TIME_TO_STIRNG;
import static com.fred.common.utils.GenericConverterFactorys.LOCAL_DATE_TO_DATE;
import static com.fred.common.utils.GenericConverterFactorys.LOCAL_DATE_TO_STIRNG;
import static com.fred.common.utils.GenericConverterFactorys.LOCAL_TIME_TO_DATE;
import static com.fred.common.utils.GenericConverterFactorys.LONG_TO_DATE;
import static com.fred.common.utils.GenericConverterFactorys.LONG_TO_LOCAL_DATE;
import static com.fred.common.utils.GenericConverterFactorys.LONG_TO_LOCAL_DATE_TIME;
import static com.fred.common.utils.GenericConverterFactorys.LONG_TO_LOCAL_TIME;
import static com.fred.common.utils.GenericConverterFactorys.STRING_TO_DATE;
import static com.fred.common.utils.GenericConverterFactorys.STRING_TO_LOCAL_DATE;
import static com.fred.common.utils.GenericConverterFactorys.STRING_TO_LOCAL_DATE_TIME;
import static com.fred.common.utils.GenericConverterFactorys.STRING_TO_LOCAL_TIME;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import lombok.experimental.UtilityClass;

/**
 * 
 * @Description: ConvertUtils
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@UtilityClass
public class ConvertUtils {

    private static final DefaultConversionService conversionService =
            new DefaultConversionService();

    static {
        applyDefaultSettings(conversionService);
    }

    public void applyDefaultSettings(DefaultConversionService conversionService) {
        conversionService.removeConvertible(String.class, Enum.class);

        conversionService.addConverterFactory(LOCAL_DATE_TO_DATE);
        conversionService.addConverterFactory(LOCAL_TIME_TO_DATE);
        conversionService.addConverterFactory(LOCAL_DATE_TIME_TO_DATE);
        conversionService.addConverterFactory(LONG_TO_DATE);
        conversionService.addConverterFactory(STRING_TO_DATE);
        conversionService.addConverterFactory(DATE_TO_LOCAL_DATE);
        conversionService.addConverterFactory(DATE_TO_LOCAL_DATE_TIME);
        conversionService.addConverterFactory(DATE_TO_LOCAL_TIME);
        conversionService.addConverterFactory(STRING_TO_LOCAL_DATE);
        conversionService.addConverterFactory(STRING_TO_LOCAL_DATE_TIME);
        conversionService.addConverterFactory(STRING_TO_LOCAL_TIME);
        conversionService.addConverterFactory(LONG_TO_LOCAL_DATE);
        conversionService.addConverterFactory(LONG_TO_LOCAL_DATE_TIME);
        conversionService.addConverterFactory(LONG_TO_LOCAL_TIME);
        conversionService.addConverterFactory(LOCAL_DATE_TO_STIRNG);
        conversionService.addConverterFactory(LOCAL_DATE_TIME_TO_STIRNG);
        conversionService.addConverterFactory(LOCAL_DATE_TIME_TO_LONG);
    }

    public <S, R> void registerConverter(Converter<S, R> converter) {
        if (converter != null) {
            conversionService.addConverter(converter);
        }
    }

    public <S, R> void registerConverterFactory(ConverterFactory<S, R> converterFactory) {
        if (converterFactory != null) {
            conversionService.addConverterFactory(converterFactory);
        }
    }

    public ConversionService getDefaultConversionService() {
        return conversionService;
    }

    public <T> T convert(Object source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }
}
