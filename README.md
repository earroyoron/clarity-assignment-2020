# clarityai-assignment
Solution for Clarity-AI assignment

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

- build the project 

`./gradlew build`

- you can run the project with gradlew run, 

  - to use the tool in "parsing" mode for a file use `--parse` as:`

    `./gradlew run --args="--parse input-file-10000.txt 1565647204351 1565733598341 Olvin"`

  - or using in "tail" mode where the file keeps monitored using `--tail` as:

    `./gradlew run --args="--tail input-file-10000.txt Mykenzi Olvin"`

## Continuous Integration (Circle-CI)

From the very first time the
project was developed using a CI tool 
to enforce a TDD and _simulate_ a "going to production from the 1st day"
approach.

- https://app.circleci.com/github/earroyoron/clarity-assignment-2020/pipelines

## Solution design notes

To monitor the file I've choose a reactive approach
where every new line is calculated just as it comes
as a stream, presenting the results every hour
but having them already calculated.

Other possible approach was using a microbatch style
so the results are calculated every hour.

It's a trade-off with memory, CPU and the time to
have the results calculated, and maybe both are valid
depending on circumstances or user requirements.

## Improvements / Pending / Known Issues

 - DateTime not provided as ms
 
# References 

- More on atomic commits: [One Commit. One Change](https://medium.com/@fagnerbrack/one-commit-one-change-3d10b10cebbf)

