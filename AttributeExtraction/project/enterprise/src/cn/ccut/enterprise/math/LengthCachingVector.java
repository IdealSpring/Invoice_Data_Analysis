package cn.ccut.enterprise.math;

interface LengthCachingVector {
    double getLengthSquared();

    void invalidateCachedLength();
}