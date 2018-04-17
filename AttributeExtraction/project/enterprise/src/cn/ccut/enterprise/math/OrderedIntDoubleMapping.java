package cn.ccut.enterprise.math;

import java.io.Serializable;

public final class OrderedIntDoubleMapping implements Serializable, Cloneable {
    static final double DEFAULT_VALUE = 0.0D;
    private int[] indices;
    private double[] values;
    private int numMappings;
    private boolean noDefault;

    OrderedIntDoubleMapping(boolean noDefault) {
        this();
        this.noDefault = noDefault;
    }

    OrderedIntDoubleMapping() {
        this(11);
    }

    OrderedIntDoubleMapping(int capacity) {
        this.noDefault = true;
        this.indices = new int[capacity];
        this.values = new double[capacity];
        this.numMappings = 0;
    }

    OrderedIntDoubleMapping(int[] indices, double[] values, int numMappings) {
        this.noDefault = true;
        this.indices = indices;
        this.values = values;
        this.numMappings = numMappings;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public int indexAt(int offset) {
        return this.indices[offset];
    }

    public void setIndexAt(int offset, int index) {
        this.indices[offset] = index;
    }

    public double[] getValues() {
        return this.values;
    }

    public void setValueAt(int offset, double value) {
        this.values[offset] = value;
    }

    public int getNumMappings() {
        return this.numMappings;
    }

    private void growTo(int newCapacity) {
        if (newCapacity > this.indices.length) {
            int[] newIndices = new int[newCapacity];
            System.arraycopy(this.indices, 0, newIndices, 0, this.numMappings);
            this.indices = newIndices;
            double[] newValues = new double[newCapacity];
            System.arraycopy(this.values, 0, newValues, 0, this.numMappings);
            this.values = newValues;
        }

    }

    private int find(int index) {
        int low = 0;
        int high = this.numMappings - 1;

        while(low <= high) {
            int mid = low + (high - low >>> 1);
            int midVal = this.indices[mid];
            if (midVal < index) {
                low = mid + 1;
            } else {
                if (midVal <= index) {
                    return mid;
                }

                high = mid - 1;
            }
        }

        return -(low + 1);
    }

    public double get(int index) {
        int offset = this.find(index);
        return offset >= 0 ? this.values[offset] : 0.0D;
    }

    public void set(int index, double value) {
        if (this.numMappings != 0 && index <= this.indices[this.numMappings - 1]) {
            int offset = this.find(index);
            if (offset >= 0) {
                this.insertOrUpdateValueIfPresent(offset, value);
            } else {
                this.insertValueIfNotDefault(index, offset, value);
            }
        } else if (!this.noDefault || value != 0.0D) {
            if (this.numMappings >= this.indices.length) {
                this.growTo(Math.max((int)(1.2D * (double)this.numMappings), this.numMappings + 1));
            }

            this.indices[this.numMappings] = index;
            this.values[this.numMappings] = value;
            ++this.numMappings;
        }

    }

    public void merge(OrderedIntDoubleMapping updates) {
        int[] updateIndices = updates.getIndices();
        double[] updateValues = updates.getValues();
        int newNumMappings = this.numMappings + updates.getNumMappings();
        int newCapacity = Math.max((int)(1.2D * (double)newNumMappings), newNumMappings + 1);
        int[] newIndices = new int[newCapacity];
        double[] newValues = new double[newCapacity];
        int k = 0;
        int i = 0;

        int j;
        for(j = 0; i < this.numMappings && j < updates.getNumMappings(); ++k) {
            if (this.indices[i] < updateIndices[j]) {
                newIndices[k] = this.indices[i];
                newValues[k] = this.values[i];
                ++i;
            } else if (this.indices[i] > updateIndices[j]) {
                newIndices[k] = updateIndices[j];
                newValues[k] = updateValues[j];
                ++j;
            } else {
                newIndices[k] = updateIndices[j];
                newValues[k] = updateValues[j];
                ++i;
                ++j;
            }
        }

        while(i < this.numMappings) {
            newIndices[k] = this.indices[i];
            newValues[k] = this.values[i];
            ++i;
            ++k;
        }

        while(j < updates.getNumMappings()) {
            newIndices[k] = updateIndices[j];
            newValues[k] = updateValues[j];
            ++j;
            ++k;
        }

        this.indices = newIndices;
        this.values = newValues;
        this.numMappings = k;
    }

    public int hashCode() {
        int result = 0;

        for(int i = 0; i < this.numMappings; ++i) {
            result = 31 * result + this.indices[i];
            result = 31 * result + (int)Double.doubleToRawLongBits(this.values[i]);
        }

        return result;
    }

    public boolean equals(Object o) {
        if (o instanceof OrderedIntDoubleMapping) {
            OrderedIntDoubleMapping other = (OrderedIntDoubleMapping)o;
            if (this.numMappings == other.numMappings) {
                for(int i = 0; i < this.numMappings; ++i) {
                    if (this.indices[i] != other.indices[i] || this.values[i] != other.values[i]) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(10 * this.numMappings);

        for(int i = 0; i < this.numMappings; ++i) {
            result.append('(');
            result.append(this.indices[i]);
            result.append(',');
            result.append(this.values[i]);
            result.append(')');
        }

        return result.toString();
    }

    public OrderedIntDoubleMapping clone() {
        return new OrderedIntDoubleMapping((int[])this.indices.clone(), (double[])this.values.clone(), this.numMappings);
    }

    public void increment(int index, double increment) {
        int offset = this.find(index);
        if (offset >= 0) {
            double newValue = this.values[offset] + increment;
            this.insertOrUpdateValueIfPresent(offset, newValue);
        } else {
            this.insertValueIfNotDefault(index, offset, increment);
        }

    }

    private void insertValueIfNotDefault(int index, int offset, double value) {
        if (!this.noDefault || value != 0.0D) {
            if (this.numMappings >= this.indices.length) {
                this.growTo(Math.max((int)(1.2D * (double)this.numMappings), this.numMappings + 1));
            }

            int at = -offset - 1;
            if (this.numMappings > at) {
                int i = this.numMappings - 1;

                for(int j = this.numMappings; i >= at; --j) {
                    this.indices[j] = this.indices[i];
                    this.values[j] = this.values[i];
                    --i;
                }
            }

            this.indices[at] = index;
            this.values[at] = value;
            ++this.numMappings;
        }

    }

    private void insertOrUpdateValueIfPresent(int offset, double newValue) {
        if (this.noDefault && newValue == 0.0D) {
            int i = offset + 1;

            for(int j = offset; i < this.numMappings; ++j) {
                this.indices[j] = this.indices[i];
                this.values[j] = this.values[i];
                ++i;
            }

            --this.numMappings;
        } else {
            this.values[offset] = newValue;
        }

    }
}
