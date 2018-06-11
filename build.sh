#!/bin/bash
export CODACY_PROJECT_TOKEN=3002ee7cacb74dfcb88e48dff8bf4ac6
set -ev
sbt clean coverage test
sbt coverageReport
sbt coverageAggregate
sbt codacyCoverage
