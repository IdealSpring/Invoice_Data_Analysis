package cn.ccut.enterprise.data;

import cn.ccut.enterprise.math.Vector;

public class Instance {
    private final Vector attrs;

    public Instance(Vector attrs) {
        this.attrs = attrs;
    }

    public double get(int index) {
        return this.attrs.getQuick(index);
    }

    public void set(int index, double value) {
        this.attrs.set(index, value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Instance)) {
            return false;
        } else {
            Instance instance = (Instance)obj;
            return this.attrs.equals(instance.attrs);
        }
    }

    public int hashCode() {
        return this.attrs.hashCode();
    }
}
