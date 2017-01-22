# Command line interpreter

### Available commands

* cat
* echo
* wc
* exit
* pwd

### Features

* piping support
* variables assignment support

### Architecture description

Implementation: parsing in loop, then dispatching pipe of command on each line

Parsing: separating by pipes, reading environment variables, then substituting environment variables into commands and creating new command instance with proper arguments

Pipes: pipes are implemented by PipeRunner class which is parameterized be left and right command. Output of left command is passed into right command as input. The fact that PipeRunner himself implements Command interfaces allows us to pipe unlimited amount of commands.

### How to build
./gradlew copyLauncher

### How to run
java -jar launcher.jar