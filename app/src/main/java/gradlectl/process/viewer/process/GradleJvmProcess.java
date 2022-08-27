package gradlectl.process.viewer.process;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;

@With
@Value
@AllArgsConstructor
@SuppressWarnings("RedundantModifiersValueLombok")
public class GradleJvmProcess {
    private final String name;
    private final String commandLine;
    private final ProcessHandle processHandle;
    private final long parentPid;
    private final String javaVersion;
    private final String gradleVersion;
    private final long childrenCount;
    private final long secondsAlive;
}
