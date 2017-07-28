# Disaster Modeling Library
Library containing calculation logic for disaster models used in [Akka Disaster](https://github.com/cliftbar/AkkaDisaster/tree/master).  This library currently has the Hurricane NWS23 model, but will eventually expand.

# JAR
The compiled JARs are under [Releases](https://github.com/cliftbar/DisasterModeling/releases)

# Importing into IntelliJ
1. Clone the repo in IntelliJ using
`File -> New -> Project From Version Control -> Git`

2. Mark the directories `src\main\scala` and `src\test\scala` as source and test roots, respectively.
  * Sources: `Right Click -> Mark Directory As -> Sources Root`
  * Tests: `Right Click -> Mark Directory As -> Test Sources Root`

3. Open build.sbt and import the project (IntelliJ should prompt you)

4. Set a Scala SDK for the project.  IntelliJ should prompt when opening a Scala file if the SDK is not set.

5. Open the Project Structure `File -> Project Structure` and under Modules, if there are more than two modules set, delete the one without the dependancies set.  The correct module to keep should have the same name (case sensitive) as the `-build` module.  Do not delete the `-build` module.

6. Make two new run configurations for assembling the JAR and running the tests:
  * Assemble JAR: `New SBT Task -> Tasks = assembly` (Note assembling the JAR will run all tests) (Assembled JARs will be under `target\scala-xxx\`)
  * Test Run: `New Scala Test -> Test Kind = All in package -> Test Package = cliftbar.disastertest`
