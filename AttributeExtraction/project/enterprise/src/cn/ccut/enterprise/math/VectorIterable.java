package cn.ccut.enterprise.math;

import java.util.Iterator;

public interface VectorIterable extends Iterable<MatrixSlice> {
    Iterator<MatrixSlice> iterateAll();

    int numSlices();

    int numRows();

    int numCols();

    Vector times(Vector var1);

    Vector timesSquared(Vector var1);
}
