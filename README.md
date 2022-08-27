# Gradlectl

Gradlectl or shorter gctl is a simple app that helps view and manage Gradle processes.

Example output:
```
❯ gctl
Name         Pid      PPid    Gradle version             Java version    Subprocesses    Uptime
Daemon       9824     1       7.5                        11.0.13+8       0               2min 19s
Daemon       3473     1       7.5.1                      11.0.13+8       0               5min 21s
Daemon       9399     9388    7.6-20220822144040+0000    11.0.13+8       13              3min 40s
Worker 12    10191    9399    7.6-20220822144040+0000    11.0.13+8       0               25s
Worker 11    10189    9399    7.6-20220822144040+0000    11.0.13+8       0               30s
Worker 10    10188    9399    7.6-20220822144040+0000    11.0.13+8       0               37s
Worker 9     10166    9399    7.6-20220822144040+0000    11.0.13+8       0               47s
Worker 8     10165    9399    7.6-20220822144040+0000    11.0.13+8       0               47s
Worker 7     10163    9399    7.6-20220822144040+0000    11.0.13+8       0               47s
Worker 6     10161    9399    7.6-20220822144040+0000    11.0.13+8       0               50s
Worker 5     10157    9399    7.6-20220822144040+0000    11.0.13+8       0               53s
Worker 3     10156    9399    7.6-20220822144040+0000    11.0.13+8       0               53s
Worker 4     10155    9399    7.6-20220822144040+0000    11.0.13+8       0               53s
Worker 2     9692     9399    7.6-20220822144040+0000    11.0.13+8       0               2min 35s
Worker 1     9616     9399    7.6-20220822144040+0000    11.0.13+8       0               2min 41s
```

# Usage

Just `gctl` or `gctl <arguments>`.

When no argument is specified, Gradlectl will print all Gradle Jvm processes.

Arguments:

| Arguments   | description                                                |
|-------------|------------------------------------------------------------|
| no argument | Prints all Gradle Jvm processes                            |
| -t          | Continuously prints all Gradle Jvm processes until stopped |
| killall     | Kills all Gradle Jvm processes                             |
| kill \<pid\> | Kills process with pid "pid"                               |
| tree \<pid\> | Prints a process tree of a process with pid "pid"          |
| help        | Prints help message                                        |


# How to install

1. `git clone git@github.com:asodja/gradlectl.git`
2. `cd gradlectl`
3. `./gradlew install`: this will install a distribution to `<path to gradlectl project>/distribution`
4. Add alias to a shell startup script (e.g. .zshrc, .bashrc etc.) to path of the distribution, or add the distribution path to `$PATH`

Example of an alias for MacOS:
`alias gctl="/Users/user/workspace/gradlectl/distribution/bin/gctl"`

Note: You can also modify the distribution installation directory with `gctl.install.dir` Gradle property or System property.

# Requirements
Gradlectl has to be run with Java11 or later.