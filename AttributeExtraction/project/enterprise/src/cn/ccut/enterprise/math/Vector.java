package cn.ccut.enterprise.math;

import cn.ccut.enterprise.data.function.DoubleDoubleFunction;
import cn.ccut.enterprise.data.function.DoubleFunction;

public interface Vector {
    String asFormatString();

    Vector assign(double var1);

    Vector assign(double[] var1);

    Vector assign(Vector var1);

    Vector assign(DoubleFunction var1);

    Vector assign(Vector var1, DoubleDoubleFunction var2);

    Vector assign(DoubleDoubleFunction var1, double var2);

    int size();

    boolean isDense();

    boolean isSequentialAccess();

    Vector clone();

    Iterable<Vector.Element> all();

    Iterable<Vector.Element> nonZeroes();

    Vector.Element getElement(int var1);

    void mergeUpdates(OrderedIntDoubleMapping var1);

    Vector divide(double var1);

    double dot(Vector var1);

    double get(int var1);

    double getQuick(int var1);

    Vector like();

    Vector minus(Vector var1);

    Vector normalize();

    Vector normalize(double var1);

    Vector logNormalize();

    Vector logNormalize(double var1);

    double norm(double var1);

    double minValue();

    int minValueIndex();

    double maxValue();

    int maxValueIndex();

    Vector plus(double var1);

    Vector plus(Vector var1);

    void set(int var1, double var2);

    void setQuick(int var1, double var2);

    void incrementQuick(int var1, double var2);

    int getNumNondefaultElements();

    int getNumNonZeroElements();

    Vector times(double var1);

    Vector times(Vector var1);

    Vector viewPart(int var1, int var2);

    double zSum();

    Matrix cross(Vector var1);

    double aggregate(DoubleDoubleFunction var1, DoubleFunction var2);

    double aggregate(Vector var1, DoubleDoubleFunction var2, DoubleDoubleFunction var3);

    double getLengthSquared();

    double getDistanceSquared(Vector var1);

    double getLookupCost();

    double getIteratorAdvanceCost();

    boolean isAddConstantTime();

    public interface Element {
        double get();

        int index();

        void set(double var1);
    }
}
