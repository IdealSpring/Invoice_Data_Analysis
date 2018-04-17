package cn.ccut.enterprise.data.function;

public abstract class DoubleFunction {
    public DoubleFunction() {
    }

    public abstract double apply(double var1);

    public boolean isDensifying() {
        return Math.abs(this.apply(0.0D)) != 0.0D;
    }
}
