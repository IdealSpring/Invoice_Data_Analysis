package cn.ccut.enterprise.data.function;

public abstract class DoubleDoubleFunction {
    public DoubleDoubleFunction() {
    }

    public abstract double apply(double var1, double var3);

    public boolean isLikeRightPlus() {
        return false;
    }

    public boolean isLikeLeftMult() {
        return false;
    }

    public boolean isLikeRightMult() {
        return false;
    }

    public boolean isLikeMult() {
        return this.isLikeLeftMult() && this.isLikeRightMult();
    }

    public boolean isCommutative() {
        return false;
    }

    public boolean isAssociative() {
        return false;
    }

    public boolean isAssociativeAndCommutative() {
        return this.isAssociative() && this.isCommutative();
    }

    public boolean isDensifying() {
        return this.apply(0.0D, 0.0D) != 0.0D;
    }
}

