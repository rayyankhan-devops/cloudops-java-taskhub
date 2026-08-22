package com.example.demo.model;

public class SystemMetrics {
    private String applicationName;
    private String status;
    private long uptimeSeconds;
    private String formattedUptime;
    private long totalMemoryMb;
    private long freeMemoryMb;
    private long usedMemoryMb;
    private double memoryUsagePercent;
    private int availableProcessors;
    private int activeThreadCount;
    private String javaVersion;
    private String javaVendor;
    private String osName;
    private String osArch;

    public SystemMetrics() {
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public void setUptimeSeconds(long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }

    public String getFormattedUptime() {
        return formattedUptime;
    }

    public void setFormattedUptime(String formattedUptime) {
        this.formattedUptime = formattedUptime;
    }

    public long getTotalMemoryMb() {
        return totalMemoryMb;
    }

    public void setTotalMemoryMb(long totalMemoryMb) {
        this.totalMemoryMb = totalMemoryMb;
    }

    public long getFreeMemoryMb() {
        return freeMemoryMb;
    }

    public void setFreeMemoryMb(long freeMemoryMb) {
        this.freeMemoryMb = freeMemoryMb;
    }

    public long getUsedMemoryMb() {
        return usedMemoryMb;
    }

    public void setUsedMemoryMb(long usedMemoryMb) {
        this.usedMemoryMb = usedMemoryMb;
    }

    public double getMemoryUsagePercent() {
        return memoryUsagePercent;
    }

    public void setMemoryUsagePercent(double memoryUsagePercent) {
        this.memoryUsagePercent = memoryUsagePercent;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public int getActiveThreadCount() {
        return activeThreadCount;
    }

    public void setActiveThreadCount(int activeThreadCount) {
        this.activeThreadCount = activeThreadCount;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getJavaVendor() {
        return javaVendor;
    }

    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }
}
