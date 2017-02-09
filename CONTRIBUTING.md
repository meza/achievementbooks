# Contributing to Achievement Books

I welcome every suggestion, bug report, feature request or pull request.

# DBAD
Before you do anything, please accept the Don't Be A ... jerk rule.

We're all here to make Minecraft better, respect eachother. Violators will not be tolerated.


# Bug request / feature request

Please, before you post anything, head on over to the [Issue list](https://github.com/meza/achievementbooks/issues?utf8=%E2%9C%93&q=is%3Aissue)
and make sure it's not a duplicate of something.

# Adding functionality
If you know how to do something better, please, by all means do. Making mods is only a hobby and I can't always be here.


1. Make sure you create your own fork of the code.
2. Create a branch for your modification
3. Run `./gradlew dependencies`
4. Run `./gradelw setupDecompWorkspace`
5. To create the mod after your modifications are done, run `./gradlew assemble`
6. Test your work by running `./gradlew runClient` or `./gradlew runServer`
7. Submit a pull request that describes what problem you were trying to solve and how.

## Using eclipse
If you're using eclipse, you can set up your workspace by:
```
./gradlew eclipse
```

## Using IntelliJ Idea
```
./gradelw idea
```
### Creating run targets
```
./gradlew genIntellijRuns
```


