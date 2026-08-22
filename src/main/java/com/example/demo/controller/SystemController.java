package com.example.demo.controller;

import com.example.demo.model.SystemMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SystemController {

    @Value("${spring.application.name:demo}")
    private String appName;

    private static final long START_TIME = System.currentTimeMillis();

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", appName,
                "timestamp", Instant.now().toString(),
                "uptimeSeconds", (System.currentTimeMillis() - START_TIME) / 1000
        ));
    }

    @GetMapping("/system/metrics")
    public ResponseEntity<SystemMetrics> getMetrics() {
        Runtime runtime = Runtime.getRuntime();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        long uptimeSec = (System.currentTimeMillis() - START_TIME) / 1000;
        long totalMem = runtime.totalMemory() / (1024 * 1024);
        long freeMem = runtime.freeMemory() / (1024 * 1024);
        long usedMem = totalMem - freeMem;
        double memPercent = totalMem > 0 ? ((double) usedMem / totalMem) * 100.0 : 0;

        SystemMetrics metrics = new SystemMetrics();
        metrics.setApplicationName(appName);
        metrics.setStatus("HEALTHY");
        metrics.setUptimeSeconds(uptimeSec);
        metrics.setFormattedUptime(formatUptime(uptimeSec));
        metrics.setTotalMemoryMb(totalMem);
        metrics.setFreeMemoryMb(freeMem);
        metrics.setUsedMemoryMb(usedMem);
        metrics.setMemoryUsagePercent(Math.round(memPercent * 10.0) / 10.0);
        metrics.setAvailableProcessors(runtime.availableProcessors());
        metrics.setActiveThreadCount(Thread.activeCount());
        metrics.setJavaVersion(System.getProperty("java.version"));
        metrics.setJavaVendor(System.getProperty("java.vendor"));
        metrics.setOsName(System.getProperty("os.name"));
        metrics.setOsArch(System.getProperty("os.arch"));

        return ResponseEntity.ok(metrics);
    }

    private String formatUptime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, secs);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, secs);
        } else {
            return String.format("%ds", secs);
        }
    }
}
