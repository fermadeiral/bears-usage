# Bears usage

## Getting started:
 
```
 git clone https://github.com/fermadeiral/bears-usage.git
 cd bears-usage
 mvn clean compile
 mvn dependency:build-classpath | egrep -v "(^\[INFO\]|^\[WARNING\])" | tee /tmp/classpath.txt
 java -cp $(cat /tmp/classpath.txt):target/classes fr.inria.spirals.bears.usage.Launcher <usage options>
```

## Usage option:

```
  [-h|--help]

  [-d|--debug]

  [--outputPath <outputPath>]
        Specify the path to output the results. (default: ./output)

  (-o|--outputMode) <METRICS|BRANCHES>
        Specify if the output is metrics (METRICS) or branch names (BRANCHES).

  [--projectNameCriterion <projectNameCriterion>]
        Search criterion: specify the name of the project (e.g. INRIA/spoon).

  [--exceptionTypeCriterion <exceptionTypeCriterion>]
        Search criterion: specify the exception type (e.g.
        java.lang.NullPointerException).

  [--initialDateCriterion <format dd/MM/yyyy>]
        Search criterion: specify the initial date to analyze branches (e.g.
        01/01/2017). Note that the search starts from 00:00:00 of the specified
        date.

  [--finalDateCriterion <format dd/MM/yyyy>]
        Search criterion: specify the final date to analyze branches (e.g.
        31/01/2017). Note that the search is until 23:59:59 of the specified
        date.

  [--maxEstimatedBuildAndTestDurationCriterion <maxEstimatedBuildAndTestDurationCriterion>]
        Search criterion: specify the maximum duration that is estimated to
        build and test a bug (e.g. 10000).

  [--maxNumberOfFailingTestsCriterion <maxNumberOfFailingTestsCriterion>]
        Search criterion: specify the maximum number of failing tests of a bug
        (e.g. 1).
```
