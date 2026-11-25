#!/bin/sh
# Install the Modelio module API (org.modelio:MDAKit and the artifacts it depends on) into a local Maven
# repository, from an OFFICIAL Modelio Open Source installation. Needed because repository.modelio.org,
# the Maven repository the module poms point to, is offline.
#   - MDAKit pom: official, from the Modelio source tag (modelio-lib/mdakit/maven/MDAKitorg/pom.xml)
#   - jars: from the installation's plugins/ and modules/*.jmdac (same binaries Modelio runs);
#     javadesigner annotations jar from the same source tag (modelio-lib/mdakit/lib)
#   - Eclipse artifacts that MDAKit pins to old versions (org.modelio:org.eclipse.*) get the jar Modelio ships
#     (compile-time API only; at runtime the module always uses the bundles of the running Modelio)
#   - javax.annotation / javax.inject come from Maven Central
# Usage: sh install-modelio-api.sh <modelio install dir> [maven repo] [tag]
#   default repo ~/tmp/_code/modelio/m2, tag v6.2.0
set -eu
A=${1:?Modelio installation directory (with plugins/ and modules/)}
REPO=${2:-$HOME/tmp/_code/modelio/m2}; TAG=${3:-v6.2.0}
MVN=${MVN:-mvn}
W=$(mktemp -d); trap 'rm -rf "$W"' EXIT
P="$A/plugins"; [ -d "$P" ] || { echo "no plugins/ in $A"; exit 1; }

curl -fsSL -o "$W/MDAKit.pom" "https://raw.githubusercontent.com/ModelioOpenSource/Modelio/$TAG/modelio-lib/mdakit/maven/MDAKitorg/pom.xml"

inst() { # file groupId artifactId version [packaging]
  "$MVN" -q -B -Dmaven.repo.local="$REPO" install:install-file -Dfile="$1" -DgroupId="$2" -DartifactId="$3" \
    -Dversion="$4" -Dpackaging="${5:-jar}" -DgeneratePom=true >/dev/null
  echo "  $2:$3:$4 <- $(basename "$1")"; }
jarof() { # bundle symbolic name -> jar (zips exploded bundle directories)
  f=$(ls -d "$P/$1"_[0-9]* 2>/dev/null | sort | tail -1); [ -n "$f" ] || { echo "missing bundle $1" >&2; exit 1; }
  if [ -d "$f" ]; then (cd "$f" && zip -qr "$W/$1.jar" .); echo "$W/$1.jar"; else echo "$f"; fi; }

echo "MDAKit ($TAG) into $REPO"
python3 -I - "$W/MDAKit.pom" > "$W/deps.txt" <<'PY'
import re,sys
t=open(sys.argv[1]).read()
mv=re.search(r"<ModelioVersion>\[([\d.]+),",t).group(1)
print("MDAKIT", re.search(r"<artifactId>MDAKit</artifactId>\s*<version>(.*?)</version>",t).group(1), mv)
for m in re.finditer(r"<dependency>(.*?)</dependency>",t,re.S):
    g=lambda k:(re.search(f"<{k}>(.*?)</{k}>",m.group(1)) or [None,""])[1]
    print(g("groupId"),g("artifactId"),g("version"))
PY
read -r _ KITVER MV < "$W/deps.txt"
QV=$(ls "$P" | sed -n 's/^org\.modelio\.platform\.api_\([0-9.]*\)\.jar$/\1/p' | head -1)   # e.g. 6.2.0.202608070650
case "$QV" in "$MV".*) ;; *) echo "installation is $QV, MDAKit $TAG expects $MV"; exit 1 ;; esac
tail -n +2 "$W/deps.txt" | while read -r g a v; do
  case "$g:$a" in
    org.modelio:org.modelio.*) inst "$(jarof "$a")" "$g" "$a" "$QV" ;;
    org.modelio:javadesigner)      # Java annotations (objid, mdl...) used by generated module code, from the source tag
      curl -fsSL -o "$W/javadesigner-$v.jar" "https://raw.githubusercontent.com/ModelioOpenSource/Modelio/$TAG/modelio-lib/mdakit/lib/javadesigner-$v.jar"
      inst "$W/javadesigner-$v.jar" "$g" "$a" "$v" ;;
    org.modelio:org.eclipse.swt.win32.win32.x86_64) inst "$(jarof org.eclipse.swt.gtk.linux.x86_64)" "$g" "$a" "$v" ;;
    org.modelio:org.eclipse.*) inst "$(jarof "$a")" "$g" "$a" "$v" ;;
    java.package:javax.annotation)
      "$MVN" -q -B -Dmaven.repo.local="$REPO" dependency:copy -Dartifact=javax.annotation:javax.annotation-api:1.3.2 -DoutputDirectory="$W" >/dev/null
      inst "$W/javax.annotation-api-1.3.2.jar" "$g" "$a" "$v" ;;
    java.package:javax.inject)
      "$MVN" -q -B -Dmaven.repo.local="$REPO" dependency:copy -Dartifact=javax.inject:javax.inject:1 -DoutputDirectory="$W" >/dev/null
      inst "$W/javax.inject-1.jar" "$g" "$a" "$v" ;;
    *) echo "  $g:$a:$v from Maven Central" ;;
  esac
done
inst "$W/MDAKit.pom" org.modelio MDAKit "$KITVER" pom
# Modules that other modules build on (official module jars)
MM=$(ls "$P"/org.modelio.modelermodule_*.jar | tail -1); v=${MM##*_}; inst "$MM" org.modelio org.modelio.modelermodule "${v%.jar}"
for m in "$A"/modules/*.jmdac; do
  n=$(basename "$m" | sed 's/_.*//'); lc=$(echo "$n" | tr 'A-Z' 'a-z')
  j=$(unzip -Z1 "$m" | grep "^$n/lib/$lc-[0-9.]*\.jar$" | head -1) || true
  [ -n "$j" ] && unzip -q -o -j "$m" "$j" -d "$W" && f="$W/$(basename "$j")" && v=${f##*-} && inst "$f" org.modelio.module "$lc" "${v%.jar}"
done
echo "done: use -Dmaven.repo.local=$REPO (or point settings.xml at it)"
