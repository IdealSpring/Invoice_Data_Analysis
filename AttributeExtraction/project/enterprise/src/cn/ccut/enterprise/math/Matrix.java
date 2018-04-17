package cn.ccut.enterprise.math;

import cn.ccut.enterprise.data.function.DoubleDoubleFunction;
import cn.ccut.enterprise.data.function.DoubleFunction;
import cn.ccut.enterprise.data.function.VectorFunction;

import java.util.Map;

public interface Matrix extends Cloneable, VectorIterable {
    String asFormatString();

    Matrix assign(double var1);

    Matrix assign(double[][] var1);

    Matrix assign(Matrix var1);

    Matrix assign(DoubleFunction var1);

    Matrix assign(Matrix var1, DoubleDoubleFunction var2);

    Matrix assignColumn(int var1, Vector var2);

    Matrix assignRow(int var1, Vector var2);

    Vector aggregateRows(VectorFunction var1);

    Vector aggregateColumns(VectorFunction var1);

    double aggregate(DoubleDoubleFunction var1, DoubleFunction var2);

    int columnSize();

    int rowSize();

    Matrix clone();

    double determinant();

    Matrix divide(double var1);

    double get(int var1, int var2);

    double getQuick(int var1, int var2);

    Matrix like();

    Matrix like(int var1, int var2);

    Matrix minus(Matrix var1);

    Matrix plus(double var1);

    Matrix plus(Matrix var1);

    void set(int var1, int var2, double var3);

    void set(int var1, double[] var2);

    void setQuick(int var1, int var2, double var3);

    int[] getNumNondefaultElements();

    Matrix times(double var1);

    Matrix times(Matrix var1);

    Matrix transpose();

    double zSum();

    Map<String, Integer> getColumnLabelBindings();

    Map<String, Integer> getRowLabelBindings();

    void setColumnLabelBindings(Map<String, Integer> var1);

    void setRowLabelBindings(Map<String, Integer> var1);

    double get(String var1, String var2);

    void set(String var1, String var2, double var3);

    void set(String var1, String var2, int var3, int var4, double var5);

    void set(String var1, double[] var2);

    void set(String var1, int var2, double[] var3);

    Matrix viewPart(int[] var1, int[] var2);

    Matrix viewPart(int var1, int var2, int var3, int var4);

    Vector viewRow(int var1);

    Vector viewColumn(int var1);

    Vector viewDiagonal();
}
