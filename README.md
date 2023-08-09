# Gradlectl

Gradlectl or shorter gctl is a simple app that helps view and manage Gradle processes.

Example output:
```
❯ gctl
Name        Pid      PPid     Gradle version             Java version    Subprocesses    Cpu        Mem        Uptime
Daemon      28380    1        7.5.1                      17.0.1+12       0               0,039%     191,0MB    10min 53s
Daemon      26269    1        7.5.1                      11.0.13+8       0               0,032%     694,7MB    5min 39s
Daemon      23655    1        7.6-20220822144040+0000    11.0.13+8       8               15,897%    3,047GB    47min 4s
Worker 8    29595    23655    7.6-20220822144040+0000    11.0.13+8       0               0,066%     392,1MB    23s
Worker 7    29591    23655    7.6-20220822144040+0000    11.0.13+8       0               0,043%     146,5MB    37s
Worker 6    29590    23655    7.6-20220822144040+0000    11.0.13+8       0               0,049%     401,4MB    40s
Worker 5    29588    23655    7.6-20220822144040+0000    11.0.13+8       0               0,159%     317,0MB    40s
Worker 4    29587    23655    7.6-20220822144040+0000    11.0.13+8       0               4,330%     224,4MB    40s
Worker 3    29489    23655    7.6-20220822144040+0000    11.0.13+8       0               0,015%     205,7MB    2min 8s
Worker 2    23763    23655    7.6-20220822144040+0000    11.0.13+8       0               0,012%     199,1MB    39min 3s
Worker 1    23768    23655    7.6-20220822144040+0000    11.0.13+8       0               0,043%     405,6MB    45min 57s
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

1. `git clone https://github.com/asodja/gradlectl.git`
2. `cd gradlectl`
3. `./gradlew install`: this will install a distribution to `<path to gradlectl project>/distribution`
4. Add alias to a shell startup script (e.g. .zshrc, .bashrc etc.) to path of the distribution, or add the distribution path to `$PATH`

Example of an alias for MacOS:
`alias gctl="/Users/user/workspace/gradlectl/distribution/bin/gctl"`

Note: You can also modify the distribution installation directory with `gctl.install.dir` Gradle property or System property.

# Requirements
Gradlectl has to be run with Java 11 or later.

# Slow execution on macOS 
Gradlectl finds active JVMs via network on 127.0.0.1 (localhost). On macOS such calls can be slow. 

To speed up the execution, add your machine local hostname to `/etc/hosts`, like:
```
127.0.0.1 localhost <hostname>
::1       localhost <hostname>
```
where `<hostname>` is value provided by `hostname` command.