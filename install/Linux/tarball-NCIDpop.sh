#!/bin/sh

rm -rf NCIDpop

#Gather the files
mkdir NCIDpop
cp ../NCIDpop.jar NCIDpop
cp ../CIDRing.wav NCIDpop
cp ../../license.txt NCIDpop
cp ../../ReverseLookupURLs.txt NCIDpop
cp ../../CONTRIBUTORS.txt NCIDpop
cp ../../CHANGES.txt NCIDpop
cp thirdPartyNameScript.sh NCIDpop

#Make the tarball!
tar czf NCIDpop-0.10.10.tar.gz NCIDpop