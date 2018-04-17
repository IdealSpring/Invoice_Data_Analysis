package cn.ccut.enterprise.math;

public class MatrixSlice extends DelegatingVector {
    private int index;

    public MatrixSlice(Vector v, int index) {
        super(v);
        this.index = index;
    }

    public Vector vector() {
        return this.getVector();
    }

    public int index() {
        return this.index;
    }
}