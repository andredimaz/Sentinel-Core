package github.andredimaz.plugin.core.utils.formatters;

public class Letter {
    private String suffix;
    private int zeros;

    public Letter(String suffix, int zeros) {
        this.suffix = suffix;
        this.zeros = zeros;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Integer getZeros() {
        return this.zeros;
    }

    public double getNumber() {
        return Double.parseDouble("1" + NumberFormatter.getZeros(this.getZeros()));
    }
}
