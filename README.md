# clarityai-assignment
Solution for Clarity-AI assignment

[![CodeFactor](https://www.codefactor.io/repository/github/earroyoron/clarity-assignment-2020/badge/master)](https://www.codefactor.io/repository/github/earroyoron/clarity-assignment-2020/overview/master)

[![earroyoron](https://circleci.com/gh/earroyoron/clarity-assignment-2020.svg?style=svg)](https://circleci.com/gh/earroyoron/clarity-assignment-2020)

## At a glance

The project is built with Kotlin, no frameworks, 
and [KotlinTest 3.3.2](https://github.com/kotest/kotest/blob/master/doc/reference.md) 
for testing.

## Usage

### Building the project

The project uses Gradle as the building tool
but _gradle wrapper is provided with the project_
so you don't need a gradle installation to build.

Just:

- Download or clone the project

`git clone https://github.com/earroyoron/clarity-assignment-2020`

In this repository you can see 
my practice of doing "atomic commits"
and "Test Driven Development" with baby-steps
in development.

Build the project with: 

`./gradlew build`

### Running

You can also run the project with gradlew: 

#### Parsing a file (task 1)

To run the tool in "parsing" mode for a file use `--parse` as the first argument as in this example:

    `./gradlew run --args="--parse src/test/resources/input-file-10000.txt 1565647204351 1565733598341 Olvin"`

The tool shows the report and exits.

#### Monitoring a file (task 2)

To run the tool in "tail" mode where the file keeps monitored use `--tail` as this other example:

    `./gradlew run --args="--tail src/test/resources/input-file-10000.txt Mykenzi Olvin"`

**FIRST REPORT IS PRINTED AFTER A DELAY, AS THE STREAM OF
DATA IS CONSUMED JUST-IN-TIME NOT AS MICROBATCH**

After the first execution or analysis we will
start waiting the required hour for the next report, 
as required in the instructions.

# Good practices in development: tools 

Some tools have been used to enforce a TDD and _simulate_ a
"going to production from the 1st day" approach.

## Continuous Integration (Circle-CI)

From the very first time the project was developed using a CI tool 

- https://app.circleci.com/github/earroyoron/clarity-assignment-2020/pipelines

### Code Factor (Code Quality testing)

A tool to check code quality and technical debt:

- https://www.codefactor.io/repository/github/earroyoron/clarity-assignment-2020

Note: I use "Sonar" but as is not free I included this tool 
that I am not very used, but a code-review tool is a needed tool for a healthy project.

# Solution And Design notes

The first task just use a _Kotlin Sequence_ to improve the
performance; using `File.useLines` we can get
a Sequence and also the file is automatically closed.

The second task is a typical **Producer-Consumer problem**
when we should take care of a fast-producer and a slow-Consumer
so we have to be aware of **back-pressure**.

(see below my notes about the possible improvements)

My solution uses a different thread for both
Producer and Consumer, and controls back-pressure
with a simple and classical JDK TransferQueue.
(not Kotlin coroutines, as I tried use them but had problems
with the context between them, and I could got a
solution with that Kotlin feature,...
I will keep trying with this for my own!)
and I use the most simple option for control
back-pressure: will just drop messages from producer 
if consumer is surpassed. 

So the solution just calculate just-in-time
the three metrics(*), and every hour we print
the figures and set the three metrics to zero.
But while we are "printing" we need and exclusive
access (to avoid keep updating data while we prepare
the results!)

Note:

I have assume for the second task that
the timestamp of each line is useless and the
tool just print "last-hour changes" in this log
without checking this data; this could be changes
if required, of course, but I thought the timestamp
was needed only for the 1st task.

## Improvements / Pending / Known Issues

 - Use can provide the time period with DateTime and not ms.
 
 - Allow multiple consumers (concurrent access to the metrics)
 and maybe using a **map-reduce** (each consumer did their metrics and
 when we need the report we reduce all)
 
 - Implement the problem with the reactive-streams API
 and a framework that implements it: JavaRx, Reactor, Akka Streams,...
 Actually my solution is a very 
 simple approach that do not fully comply with that specification.
 
# References 

- More on atomic commits: [One Commit. One Change](https://medium.com/@fagnerbrack/one-commit-one-change-3d10b10cebbf)
- http://www.reactive-streams.org/



