#! /bin/bash

type_path=$1

cd $(type_path)

rm -r codeql-db/
codeql database create codeql-db --language=java --source-root . > codeql_build.log
codeql database analyze codeql-db/ /media/data/nacoop/codeql-home/codeql-repo/java/ql/src/codeql-suites/java-code-scanning.qls --format=sarifv2.1.0 --output=reports/codeql-suite-code-scanning-results.sarif --threads 0
codeql database analyze codeql-db/ /media/data/nacoop/codeql-home/codeql-repo/java/ql/src/codeql-suites/java-lgtm-full.qls --format=sarifv2.1.0 --output=reports/codeql-suite-lgtm-full-results.sarif --threads 0
