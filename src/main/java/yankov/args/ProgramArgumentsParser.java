package yankov.args;

import yankov.args.annotations.ProgramArgument;
import yankov.args.annotations.ProgramFlag;
import yankov.args.annotations.ProgramOption;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProgramArgumentsParser {
    public static boolean parse(String[] args, Object programArguments) {
        return Arrays
                .stream(programArguments.getClass().getDeclaredFields())
                .allMatch(field -> {
                            if (field.isAnnotationPresent(ProgramOption.class)) {
                                String shortName = field.getDeclaredAnnotation(ProgramOption.class).shortName();
                                String longName = field.getDeclaredAnnotation(ProgramOption.class).longName();
                                String defaultValue = field.getDeclaredAnnotation(ProgramOption.class).defaultValue();
                                Optional<String> parsedValue = getOptionValue(args, shortName, longName);
                                String value = parsedValue.filter(x -> !x.isEmpty()).orElse(defaultValue);
                                return setStringField(field, programArguments, value);
                            } else if (field.isAnnotationPresent(ProgramFlag.class)) {
                                String shortName = field.getDeclaredAnnotation(ProgramFlag.class).shortName();
                                String longName = field.getDeclaredAnnotation(ProgramFlag.class).longName();
                                if (isOptionFlagPresent(args, shortName, longName)) {
                                    return setFlagField(field, programArguments);
                                }
                            } else if (field.isAnnotationPresent(ProgramArgument.class)) {
                                int order = field.getDeclaredAnnotation(ProgramArgument.class).order();
                                Optional<String> argument = getArgument(args, order);
                                String defaultValue = field.getDeclaredAnnotation(ProgramArgument.class).defaultValue();
                                return setStringField(field, programArguments, argument.orElse(defaultValue));
                            }
                            return true;
                        }
                );
    }

    private static boolean setStringField(Field field, Object obj, String value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    private static boolean setFlagField(Field field, Object obj) {
        field.setAccessible(true);
        try {
            field.setBoolean(obj, true);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    private static Optional<String> getOptionValue(String[] args, String shortName, String longName) {
        Optional<String> fromShort = Arrays.stream(args)
                .filter(ProgramArgumentsParser::isShortOption)
                .filter(x -> getOptionName(x, false).equals(shortName))
                .findFirst();
        Optional<String> fromLong = Arrays.stream(args)
                .filter(ProgramArgumentsParser::isLongOption)
                .filter(x -> getOptionName(x, true).equals(longName))
                .findFirst();
        return fromShort.isPresent()
                ? fromShort.map(x -> trimPrefix(x).substring(1))
                : fromLong.map(x -> {
                    String withoutPrefix = trimPrefix(x);
                    if (withoutPrefix.contains(Const.eq)) {
                        return withoutPrefix.split(Const.eq)[1];
                    } else {
                        return "";
                    }
        });
    }

    private static boolean isOptionFlagPresent(String[] args, String shortName, String longName) {
        boolean presentShort = Arrays.stream(args)
                .filter(ProgramArgumentsParser::isShortOption)
                .anyMatch(x -> getOptionName(x, false).equals(shortName));
        boolean presentLong = Arrays.stream(args)
                .filter(ProgramArgumentsParser::isLongOption)
                .anyMatch(x -> getOptionName(x, true).equals(longName));
        return presentShort || presentLong;
    }

    private static boolean isShortOption(String s) {
        return s.startsWith(Const.hyphen) && !s.startsWith(Const.doubleHyphen);
    }

    private static boolean isLongOption(String s) {
        return s.startsWith(Const.doubleHyphen);
    }

    private static Optional<String> getArgument(String[] args, int order) {
        List<String> arguments = Arrays.stream(args).filter(x -> !x.startsWith(Const.hyphen)).collect(Collectors.toList());
        return (order >= 0) && (order < arguments.size()) ? Optional.of(arguments.get(order)) : Optional.empty();
    }

    private static String trimPrefix(String s) {
        String withoutPrefix = s;
        while (withoutPrefix.startsWith(Const.hyphen)) {
            withoutPrefix = withoutPrefix.substring(1);
        }
        return withoutPrefix;
    }

    private static String getOptionName(String s, boolean longOption) {
        String withoutPrefix = trimPrefix(s);
        if (longOption) {
            return withoutPrefix.contains(Const.eq) ? withoutPrefix.split(Const.eq)[0] : withoutPrefix;
        }
        return withoutPrefix.substring(0, 1);
    }
}
