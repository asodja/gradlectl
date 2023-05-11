package gradlectl.process.viewer.console;

import gradlectl.process.viewer.process.GradleJvmProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsolePrinter {

    private static final int TABLE_VALUE_MAX_LENGTH = 30;
    private static final int TABLE_COLUMNS_OFFSET = 4;

    public void printGradleProcessTable(List<GradleJvmProcess> processes) {
        List<TableColumn> columns = new ArrayList<>();
        for (TableHeader tableHeader : TableHeader.values()) {
            TableColumn column = new TableColumn(tableHeader);
            columns.add(column);
            for (GradleJvmProcess process : processes) {
                String value = tableHeader.getValue(process);
                column.add(value.substring(0, Math.min(TABLE_VALUE_MAX_LENGTH, value.length())));
            }
        }

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < processes.size() + 1; i++) {
            for (TableColumn column : columns) {
                output.append(String.format("%-" + (column.getMaxWidth() + TABLE_COLUMNS_OFFSET) + "s", column.getValue(i)));
            }
            output.append("\n");
        }
        System.out.print(output);
    }

    public void printProcessTree(long pid) {
        ProcessHandle.of(pid).ifPresent(it -> printProcessAtDepth(it, 0));
    }

    private void printProcessAtDepth(ProcessHandle processHandle, int depth) {
        String spaces = " ".repeat(depth * 4);
        String arguments = Stream.of(processHandle.info().arguments().orElse(new String[0]))
                .filter(it -> !it.isEmpty() && !it.startsWith("-") && !it.startsWith("@"))
                .collect(Collectors.joining(" "));
        System.out.printf("%s \\- %d: %s %s%n", spaces, processHandle.pid(), processHandle.info().command().orElse("Unknown"), arguments);
        processHandle.children().forEach(it -> printProcessAtDepth(it, depth + 1));
    }
}
