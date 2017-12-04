
package com.shykhmat.jmetrics.core.report;

public class Metrics {
    private Double halsteadVolume = 0.;
    private Integer cyclomaticComplexity = 0;
    private Integer linesOfCode = 0;
    private Double maintainabilityIndex = 0.;

    public Double getHalsteadVolume() {
        return halsteadVolume;
    }

    public void setHalsteadVolume(Double halsteadVolume) {
        this.halsteadVolume = halsteadVolume;
    }

    public Integer getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(Integer cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public Integer getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(Integer linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public Double getMaintainabilityIndex() {
        return maintainabilityIndex;
    }

    public void setMaintainabilityIndex(Double maintainabilityIndex) {
        this.maintainabilityIndex = maintainabilityIndex;
    }

}
