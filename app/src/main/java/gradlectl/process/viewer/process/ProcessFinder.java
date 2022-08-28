package gradlectl.process.viewer.process;

import lombok.SneakyThrows;
import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessFinder {

    private static final String DAEMON_MAIN = "org.gradle.launcher.daemon.bootstrap.GradleDaemon";
    private static final String WORKER_MAIN = "worker.org.gradle.process.internal.worker.GradleWorkerMain";
    private static final String KOTLIN_COMPILE_DAEMON_MAIN = "org.jetbrains.kotlin.daemon.KotlinCompileDaemon";

    @SneakyThrows
    public List<GradleJvmProcess> findGradleJvmProcesses() {
        Instant now = Instant.now();
        HostIdentifier hostIdentifier = new HostIdentifier("local://127.0.0.1?mode=r");
        MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(hostIdentifier);
        return findGradleDaemonsAndWorkers(monitoredHost, monitoredHost.activeVms(), now);
    }

    private List<GradleJvmProcess> findGradleDaemonsAndWorkers(MonitoredHost monitoredHost, Set<Integer> activeVms, Instant now) {
        return activeVms.stream()
                .map(vmPid -> toGradleJvmProcess(monitoredHost, vmPid, DAEMON_MAIN, "Unknown", now))
                .filter(Optional::isPresent)
                .flatMap(daemonOptional -> {
                    String commandLine = daemonOptional.get().getCommandLine();
                    String gradleVersion = commandLine.replace("org.gradle.launcher.daemon.bootstrap.GradleDaemon", "").trim();
                    GradleJvmProcess daemon = daemonOptional.get()
                            .withName("Daemon")
                            .withGradleVersion(gradleVersion);
                    List<GradleJvmProcess> processes = new ArrayList<>();
                    processes.add(daemon);
                    processes.addAll(findChildGradleWorkers(monitoredHost, daemon.getProcessHandle(), gradleVersion, now));
                    processes.addAll(findChildKotlinDaemons(monitoredHost, daemon.getProcessHandle(), gradleVersion, now));
                    return processes.stream();
                }).collect(Collectors.toList());
    }

    private List<GradleJvmProcess> findChildGradleWorkers(MonitoredHost monitoredHost, ProcessHandle parentProcessHandle, String gradleVersion, Instant now) {
        return parentProcessHandle.children()
                .map(child -> toGradleJvmProcess(monitoredHost, (int) child.pid(), WORKER_MAIN, gradleVersion, now))
                .filter(Optional::isPresent)
                .map(it -> it.get().withName("Worker " + it.get().getCommandLine()
                        .replace("worker.org.gradle.process.internal.worker.GradleWorkerMain", "")
                        .replace("'", "")
                        .replace("Gradle Worker Daemon", "")
                        .trim()))
                .collect(Collectors.toList());
    }

    private List<GradleJvmProcess> findChildKotlinDaemons(MonitoredHost monitoredHost, ProcessHandle parentProcessHandle, String gradleVersion, Instant now) {
        return parentProcessHandle.children()
                .map(child -> toGradleJvmProcess(monitoredHost, (int) child.pid(), KOTLIN_COMPILE_DAEMON_MAIN, gradleVersion, now))
                .filter(Optional::isPresent)
                .map(it -> it.get().withName("KotlinCompileDaemon"))
                .collect(Collectors.toList());
    }

    private Optional<GradleJvmProcess> toGradleJvmProcess(MonitoredHost monitoredHost, int vmPid, String mainClass, String gradleVersion, Instant now) {
        try {
            String vmIdString = "//" + vmPid + "?mode=r";
            MonitoredVm vm = monitoredHost.getMonitoredVm(new VmIdentifier(vmIdString));
            String commandLine = MonitoredVmUtil.commandLine(vm);
            Optional<ProcessHandle> processHandleOptional = ProcessHandle.of(vmPid);
            if (!commandLine.startsWith(mainClass) || processHandleOptional.isEmpty()) {
                return Optional.empty();
            }

            ProcessHandle processHandle = processHandleOptional.get();
            String vmVersion = MonitoredVmUtil.vmVersion(vm);
            long secondsAlive = processHandle.info().startInstant().map(it -> Duration.between(it, now).getSeconds()).orElse(-1L);
            long childrenCount = processHandle.descendants().count();
            long parentPid = processHandle.parent().map(ProcessHandle::pid).orElse(-1L);
            return Optional.of(new GradleJvmProcess(commandLine, commandLine, processHandle, parentPid, vmVersion, gradleVersion, childrenCount, secondsAlive));
        } catch (MonitorException | URISyntaxException ignored) {
            return Optional.empty();
        }
    }
}
