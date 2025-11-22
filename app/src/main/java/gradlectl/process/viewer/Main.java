package gradlectl.process.viewer;

import com.google.common.primitives.Longs;
import gradlectl.process.viewer.console.ConsolePrinter;
import gradlectl.process.viewer.process.GradleJvmProcess;
import gradlectl.process.viewer.process.ProcessFinder;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printAll();
        } else if (args[0].equals("-t")) {
            printAllUntilStopped();
        } else if (args[0].equals("tree")) {
            parseLong(args, 1).ifPresent(Main::printChildTree);
        } else if (args[0].equals("kill")) {
            parseLong(args, 1).ifPresent(Main::kill);
        } else if (args[0].equalsIgnoreCase("killall")) {
            killAll();
        } else if (args[0].equals("help")) {
            printHelp();
        } else {
            System.out.println("Unknown command: '" + args[0] + "'.");
            printHelp();
        }
    }

    private static void printHelp() {
        String message = "\nGradlectl is a mini app that helps you view and manage Gradle processes.\n" +
                "-------------------------\n" +
                "Usage:\n" +
                "gctl or gctl <arguments>\n" +
                "\n" +
                "When no argument is specified, Gradlectl will print all Gradle Jvm processes.\n" +
                "\n" +
                "Arguments:\n" +
                "no argument       Prints all Gradle Jvm processes\n" +
                "-t                Prints all Gradle Jvm processes until stopped\n" +
                "killall           Kills all Gradle Jvm processes\n" +
                "kill <pid>        Kills process with pid \"pid\"\n" +
                "tree <pid>        Prints a process tree of a process with pid \"pid\"\n" +
                "help              Prints this message\n";
        System.out.println(message);
    }

    private static void killAll() {
        List<GradleJvmProcess> daemons = new ProcessFinder().findGradleJvmProcesses();
        daemons.forEach(daemon -> daemon.getProcessHandle().destroyForcibly());
        System.out.printf("Killed %d daemons%n", daemons.size());
    }

    private static void printAll() {
        List<GradleJvmProcess> processes = new ProcessFinder().findGradleJvmProcesses();;
        new ConsolePrinter().printGradleProcessTable(processes);
    }

    @SneakyThrows
    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    private static void printAllUntilStopped() {
        while (true) {
            printAll();
            Thread.sleep(1500);
        }
    }

    private static void printChildTree(long pid) {
        new ConsolePrinter().printProcessTree(pid);
    }

    private static void kill(long pid) {
        ProcessHandle.of(pid).ifPresent(ProcessHandle::destroyForcibly);
    }

    private static Optional<Long> parseLong(String[] args, int index) {
        if (args.length >= index - 1 && Longs.tryParse(args[index]) != null) {
            return Optional.of(Long.parseLong(args[index]));
        }
        return Optional.empty();
    }
}
