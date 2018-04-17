package cn.ccut.enterprise.math;

import cn.ccut.enterprise.data.function.DoubleDoubleFunction;
import cn.ccut.enterprise.data.function.DoubleFunction;

public class DelegatingVector implements Vector, LengthCachingVector {
    protected Vector delegate;

    public DelegatingVector(Vector v) {
        this.delegate = v;
    }

    protected DelegatingVector() {
    }

    public Vector getVector() {
        return this.delegate;
    }

    public double aggregate(DoubleDoubleFunction aggregator, DoubleFunction map) {
        return this.delegate.aggregate(aggregator, map);
    }

    public double aggregate(Vector other, DoubleDoubleFunction aggregator, DoubleDoubleFunction combiner) {
        return this.delegate.aggregate(other, aggregator, combiner);
    }

    public Vector viewPart(int offset, int length) {
        return this.delegate.viewPart(offset, length);
    }

    public Vector clone() {
        DelegatingVector r;
        try {
            r = (DelegatingVector)super.clone();
        } catch (CloneNotSupportedException var3) {
            throw new RuntimeException("Clone not supported for DelegatingVector, shouldn't be possible");
        }

        r.delegate = this.delegate.clone();
        return r;
    }

    public Iterable<Element> all() {
        return this.delegate.all();
    }

    public Iterable<Element> nonZeroes() {
        return this.delegate.nonZeroes();
    }

    public Vector divide(double x) {
        return this.delegate.divide(x);
    }

    public double dot(Vector x) {
        return this.delegate.dot(x);
    }

    public double get(int index) {
        return this.delegate.get(index);
    }

    public Element getElement(int index) {
        return this.delegate.getElement(index);
    }

    public void mergeUpdates(OrderedIntDoubleMapping updates) {
        this.delegate.mergeUpdates(updates);
    }

    public Vector minus(Vector that) {
        return this.delegate.minus(that);
    }

    public Vector normalize() {
        return this.delegate.normalize();
    }

    public Vector normalize(double power) {
        return this.delegate.normalize(power);
    }

    public Vector logNormalize() {
        return this.delegate.logNormalize();
    }

    public Vector logNormalize(double power) {
        return this.delegate.logNormalize(power);
    }

    public double norm(double power) {
        return this.delegate.norm(power);
    }

    public double getLengthSquared() {
        return this.delegate.getLengthSquared();
    }

    public void invalidateCachedLength() {
        if (this.delegate instanceof LengthCachingVector) {
            ((LengthCachingVector)this.delegate).invalidateCachedLength();
        }

    }

    public double getDistanceSquared(Vector v) {
        return this.delegate.getDistanceSquared(v);
    }

    public double getLookupCost() {
        return this.delegate.getLookupCost();
    }

    public double getIteratorAdvanceCost() {
        return this.delegate.getIteratorAdvanceCost();
    }

    public boolean isAddConstantTime() {
        return this.delegate.isAddConstantTime();
    }

    public double maxValue() {
        return this.delegate.maxValue();
    }

    public int maxValueIndex() {
        return this.delegate.maxValueIndex();
    }

    public double minValue() {
        return this.delegate.minValue();
    }

    public int minValueIndex() {
        return this.delegate.minValueIndex();
    }

    public Vector plus(double x) {
        return this.delegate.plus(x);
    }

    public Vector plus(Vector x) {
        return this.delegate.plus(x);
    }

    public void set(int index, double value) {
        this.delegate.set(index, value);
    }

    public Vector times(double x) {
        return this.delegate.times(x);
    }

    public Vector times(Vector x) {
        return this.delegate.times(x);
    }

    public double zSum() {
        return this.delegate.zSum();
    }

    public Vector assign(double value) {
        this.delegate.assign(value);
        return this;
    }

    public Vector assign(double[] values) {
        this.delegate.assign(values);
        return this;
    }

    public Vector assign(Vector other) {
        this.delegate.assign(other);
        return this;
    }

    public Vector assign(DoubleDoubleFunction f, double y) {
        this.delegate.assign(f, y);
        return this;
    }

    public Vector assign(DoubleFunction function) {
        this.delegate.assign(function);
        return this;
    }

    public Vector assign(Vector other, DoubleDoubleFunction function) {
        this.delegate.assign(other, function);
        return this;
    }

    public Matrix cross(Vector other) {
        return this.delegate.cross(other);
    }

    public int size() {
        return this.delegate.size();
    }

    public String asFormatString() {
        return this.delegate.asFormatString();
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object o) {
        return this.delegate.equals(o);
    }

    public String toString() {
        return this.delegate.toString();
    }

    public boolean isDense() {
        return this.delegate.isDense();
    }

    public boolean isSequentialAccess() {
        return this.delegate.isSequentialAccess();
    }

    public double getQuick(int index) {
        return this.delegate.getQuick(index);
    }

    public Vector like() {
        return new DelegatingVector(this.delegate.like());
    }

    public void setQuick(int index, double value) {
        this.delegate.setQuick(index, value);
    }

    public void incrementQuick(int index, double increment) {
        this.delegate.incrementQuick(index, increment);
    }

    public int getNumNondefaultElements() {
        return this.delegate.getNumNondefaultElements();
    }

    public int getNumNonZeroElements() {
        return this.delegate.getNumNonZeroElements();
    }
}
