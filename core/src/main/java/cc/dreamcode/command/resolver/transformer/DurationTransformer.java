package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// duration-parser used from okaeri-command project, perfect solution <#
// https://github.com/OkaeriPoland/okaeri-commands/blob/master/core/src/main/java/eu/okaeri/commands/type/resolver/DurationTypeResolver.java
public class DurationTransformer implements ObjectTransformer<Duration> {

    private static final Pattern SIMPLE_DURATION_PATTERN = Pattern.compile("(?<value>-?[0-9]+)(?<unit>ms|ns|d|h|m|s)");
    private static final Pattern JBOD_FULL_DURATION_PATTERN = Pattern.compile("((-?[0-9]+)(ms|ns|d|h|m|s))+");

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Duration.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Duration> transform(@NonNull Class<?> type, @NonNull String input) {
        return readJbodPattern(input);
    }

    /**
     * Converts raw units to {@link Duration}.
     *
     * @param longValue amount of units
     * @param unit      string unit representation
     * @return resolved duration
     */
    private static Duration timeToDuration(long longValue, String unit) {
        switch (unit) {
            case "d":
                return Duration.ofDays(longValue);
            case "h":
                return Duration.ofHours(longValue);
            case "m":
                return Duration.ofMinutes(longValue);
            case "s":
                return Duration.ofSeconds(longValue);
            case "ms":
                return Duration.ofMillis(longValue);
            case "ns":
                return Duration.ofNanos(longValue);
            default:
                throw new IllegalArgumentException("Really, this one should not be possible: " + unit);
        }
    }

    private static Optional<Duration> readJbodPattern(String text) {

        text = text.toLowerCase(Locale.ROOT);
        text = text.replace(" ", "");

        Matcher fullMatcher = JBOD_FULL_DURATION_PATTERN.matcher(text);
        if (!fullMatcher.matches()) {
            return Optional.empty();
        }

        Matcher matcher = SIMPLE_DURATION_PATTERN.matcher(text);
        boolean matched = false;
        BigInteger currentValue = BigInteger.valueOf(0);

        while (matcher.find()) {
            matched = true;
            long longValue = Long.parseLong(matcher.group("value"));
            String unit = matcher.group("unit");
            currentValue = currentValue.add(BigInteger.valueOf(timeToDuration(longValue, unit).toNanos()));
        }

        return matched ? Optional.of(Duration.ofNanos(currentValue.longValueExact())) : Optional.empty();
    }
}
