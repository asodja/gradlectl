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
