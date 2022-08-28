package gradlectl.process.viewer.console;

import gradlectl.process.viewer.process.GradleJvmProcess;

import java.util.concurrent.TimeUnit;

enum TableHeader {
    NAME("Name") {
        @Override
        public String getValue(GradleJvmProcess process) {
            return process.getName();
        }
    },
    PID("Pid") {
        @Override
        public String getValue(GradleJvmProcess process) {
            return process.getProcessHandle().pid() + "";
        }
    },
    PPID("PPid") {
        @Override
        public String getValue(GradleJvmProcess process) {
            return process.getParentPid() + "";
        }
    },
    GRADLE_VERSION("Gradle version") {
        @Override
        public String getValue(GradleJvmProcess process) {
            return process.getGradleVersion();
        }
    },
    JAVA_VERSION("Java version") {
        @Override
        public String getValue(GradleJvmProcess process) {
            return process.getJavaVersion();
        }
    },
    SUBPROCESSES("Subprocesses") {
        @Override
        public String getValue(GradleJvmProcess process) {
            return process.getChildrenCount() + "";
        }
    },
    CPU("Cpu") {
        @Override
        public String getValue(GradleJvmProcess process) {
            if (process.getStats().getCpuUsage() <= 0) {
                return "N/A";
            }
            return String.format("%.3f", process.getStats().getCpuUsage() * 100) + "%";
        }
    },
    MEMORY("Mem") {
        @Override
        public String getValue(GradleJvmProcess process) {
            if (process.getStats().getHeapMemoryBytes() < 0 && process.getStats().getNonHeapMemoryBytes() < 0) {
                return "N/A";
            }
            long memoryBytes = 0;
            if (process.getStats().getHeapMemoryBytes() > 0) {
                memoryBytes += process.getStats().getHeapMemoryBytes();
            }
            if (process.getStats().getNonHeapMemoryBytes() > 0) {
                memoryBytes += process.getStats().getNonHeapMemoryBytes();
            }
            if (memoryBytes > 1_000_000_000) {
                return String.format("%.3f", memoryBytes / 1_000_000_000.0) + "GB";
            } else {
                return String.format("%.1f", memoryBytes / 1_000_000.0) + "MB";
            }
        }
    },
    UPTIME("Uptime") {
        @Override
        public String getValue(GradleJvmProcess process) {
            if (process.getSecondsAlive() < 0) {
                return "0s";
            }
            long hours = TimeUnit.SECONDS.toHours(process.getSecondsAlive());
            long minutes = TimeUnit.SECONDS.toMinutes(process.getSecondsAlive()) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = process.getSecondsAlive() % 60;
            if (hours > 0) {
                return String.format("%dh %dmin %ds", hours, minutes, seconds);
            } else if (minutes > 0) {
                return String.format("%dmin %ds", minutes, seconds);
            } else {
                return String.format("%ds", seconds);
            }
        }
    }
    ;

    private final String name;

    TableHeader(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract String getValue(GradleJvmProcess process);
}
