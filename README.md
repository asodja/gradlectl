# Gradlectl

Gradlectl or shorter gctl is a simple app that helps view and manage Gradle processes.

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