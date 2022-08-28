package gradlectl.process.viewer.process;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@SuppressWarnings("RedundantModifiersValueLombok")
public class GradleJvmProcessStats {
    private final double cpuUsage;
    private final long heapMemoryBytes;
    private final long nonHeapMemoryBytes;
}
