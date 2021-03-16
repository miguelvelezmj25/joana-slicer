#!/usr/bin/env bash

BASE=$(pwd)
BASE=$BASE/../../../../../../../../../..
cd "$BASE" || exit

PROGRAM=${1}
PORT=${2}

java -cp ./target/classes:../joana/dist/joana.api.jar:$HOME/.m2/repository/org/json/json/20201115/json-20201115.jar:$HOME/.m2/repository/com/fasterxml/jackson/dataformat/jackson-dataformat-xml/2.8.9/jackson-dataformat-xml-2.8.9.jar:$HOME/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:$HOME/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.0/jackson-annotations-2.8.0.jar:$HOME/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar:$HOME/.m2/repository/com/fasterxml/jackson/module/jackson-module-jaxb-annotations/2.8.9/jackson-module-jaxb-annotations-2.8.9.jar \
  edu.cmu.cs.mvelezce.joana.slicer.server.SlicingServer "$PROGRAM" "$PORT"

#java -cp ./lc-hotspot-diff-server/target/classes:./lc-perf-profile-viz/target/classes:./target/classes:$HOME/.m2/repository/org/json/json/20201115/json-20201115.jar:$HOME/.m2/repository/edu/cmu/cs/mvelezce/cc-analysis/0.2.0-SNAPSHOT/cc-analysis-0.2.0-SNAPSHOT.jar:$HOME/.m2/repository/guru/nidi/graphviz-java/0.18.0/graphviz-java-0.18.0.jar:$HOME/.m2/repository/guru/nidi/com/kitfox/svgSalamander/1.1.3/svgSalamander-1.1.3.jar:$HOME/.m2/repository/com/fasterxml/jackson/dataformat/jackson-dataformat-xml/2.8.9/jackson-dataformat-xml-2.8.9.jar:$HOME/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:$HOME/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.0/jackson-annotations-2.8.0.jar:$HOME/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar:$HOME/.m2/repository/com/fasterxml/jackson/module/jackson-module-jaxb-annotations/2.8.9/jackson-module-jaxb-annotations-2.8.9.jar:$HOME/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar:$HOME/.m2/repository/edu/cmu/cs/mvelezce/cc-execute/0.2.0-SNAPSHOT/cc-execute-0.2.0-SNAPSHOT.jar:$HOME/.m2/repository/com/google/guava/guava/28.1-jre/guava-28.1-jre.jar \
#  edu.cmu.cs.mvelezce.lc.hotspot.diff.server.HotspotDiffServer


