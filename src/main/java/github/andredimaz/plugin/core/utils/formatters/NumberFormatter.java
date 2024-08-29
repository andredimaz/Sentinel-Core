package github.andredimaz.plugin.core.utils.formatters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;

public class NumberFormatter {
    private static final Pattern LETTER_PATTERN = Pattern.compile("[^a-zA-Z]+");
    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,###,###");
    private static final Letter[] LETTERS = new Letter[34];

    static {
        LETTERS[0] = new Letter("k", 3);
        LETTERS[1] = new Letter("KK", 6);
        LETTERS[2] = new Letter("B", 9);
        LETTERS[3] = new Letter("T", 12);
        LETTERS[4] = new Letter("Q", 15);
        LETTERS[5] = new Letter("QQ", 18);
        LETTERS[6] = new Letter("S", 21);
        LETTERS[7] = new Letter("SS", 24);
        LETTERS[8] = new Letter("OC", 27);
        LETTERS[9] = new Letter("N", 30);
        LETTERS[10] = new Letter("D", 33);
        LETTERS[11] = new Letter("UN", 36);
        LETTERS[12] = new Letter("DD", 39);
        LETTERS[13] = new Letter("TD", 42);
        LETTERS[14] = new Letter("QD", 45);
        LETTERS[15] = new Letter("QQD", 48);
        LETTERS[16] = new Letter("SD", 51);
        LETTERS[17] = new Letter("SSD", 54);
        LETTERS[18] = new Letter("OCD", 57);
        LETTERS[19] = new Letter("ND", 60);
        LETTERS[20] = new Letter("V", 63);
        LETTERS[21] = new Letter("UVG", 66);
        LETTERS[22] = new Letter("DVG", 69);
        LETTERS[23] = new Letter("TVG", 72);
        LETTERS[24] = new Letter("QTV", 75);
        LETTERS[25] = new Letter("QNV", 78);
        LETTERS[26] = new Letter("SEV", 81);
        LETTERS[27] = new Letter("SPG", 84);
        LETTERS[28] = new Letter("OVG", 87);
        LETTERS[29] = new Letter("NVG", 90);
        LETTERS[30] = new Letter("TGN", 93);
        LETTERS[31] = new Letter("TGN", 93);
        LETTERS[32] = new Letter("UTG", 96);
        LETTERS[33] = new Letter("DTG", 99);
    }

    public static void setup() {
    }

    public static String format(double value) {
        return format(value, value, 0);
    }

    private static String format(double exactValue, double n, int iteration) {
        if (exactValue < 1000.0D) {
            return formatNum(exactValue);
        } else {
            double d = n / 100.0D / 10.0D;
            if (d < 1000.0D) {
                Letter letter = LETTERS[iteration];
                return String.valueOf(formatString(exactValue, letter)) + letter.getSuffix();
            } else {
                return format(exactValue, d, iteration + 1);
            }
        }
    }

    private static String formatString(Double value, Letter letter) {
        if (value == 0.0D) {
            return "0";
        } else {
            String stringValue = FORMATTER.format(value);
            stringValue = stringValue.substring(3, stringValue.length() - 3).replace(".", "");
            if (stringValue.length() == letter.getZeros() + 1) {
                return stringValue.substring(0, 1);
            } else if (stringValue.length() == letter.getZeros() + 2) {
                return stringValue.substring(0, 2);
            } else {
                return stringValue.length() == letter.getZeros() + 3 ? stringValue.substring(0, 3) : "Error";
            }
        }
    }

    public static String getLetter(int i) {
        return i < 0 ? "" : String.valueOf(getLetter(i / 26 - 1)) + (char)(65 + i % 26);
    }

    public static String getLetters(String input) {
        return LETTER_PATTERN.matcher(input).replaceAll("");
    }

    public static Letter getSuffix(String format) {
        String formatted = getLetters(format.toUpperCase());
        return (Letter)Stream.of(LETTERS).filter((x) -> x != null && formatted.equals(x.getSuffix().toUpperCase())).findFirst().orElse((Letter) null);
    }

    public static String getZeros(int amount) {
        return StringUtils.repeat("0", amount);
    }

    public static String formatLong(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    public static String formatNum(double number) {
        return DECIMAL_FORMAT.format(number);
    }
}
