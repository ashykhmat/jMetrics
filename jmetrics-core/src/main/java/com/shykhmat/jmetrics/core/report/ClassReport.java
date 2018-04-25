package com.shykhmat.jmetrics.core.report;

import java.util.Set;
import java.util.TreeSet;

/**
 * Class that contains report information for java class.
 */
public class ClassReport extends CodePartReport implements Comparable<ClassReport>{
    private Set<MethodReport> methods;
    private boolean isInterface;

    public ClassReport(String name) {
        super(name);
        methods = new TreeSet<>();
    }

    public Set<MethodReport> getMethods() {
        return methods;
    }

    public void setMethods(Set<MethodReport> methods) {
        this.methods = methods;
    }

    public void addMethod(MethodReport method) {
        methods.add(method);
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }

    @Override
    public int compareTo(ClassReport classToCompare) {
        if (getMetrics().getMaintainabilityIndex() == classToCompare.getMetrics().getMaintainabilityIndex()){
            return getName().compareTo(classToCompare.getName());
        }
        if (getMetrics().getMaintainabilityIndex() > classToCompare.getMetrics().getMaintainabilityIndex()){
            return 1;
        }
        return -1;
    }

    
}
