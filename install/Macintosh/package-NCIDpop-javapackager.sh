#!/bin/zsh -e

VERSION=0.10.10
PRODUCT="NCIDpop"
DEPLOY_DIR=deploy
SOURCE_DIR=source
DSTROOT=destpackage
DMG="$PRODUCT-MacOSX-$VERSION.dmg" 
VOL="$PRODUCT $VERSION"

# clean
sudo rm -rf $DEPLOY_DIR
sudo rm -rf $SOURCE_DIR
sudo rm -rf $DSTROOT
rm -f $DMG

# stage source files
mkdir -p $SOURCE_DIR
cp ../NCIDpop.jar $SOURCE_DIR
cp ../CIDRing.wav $SOURCE_DIR

# create disk image
javapackager -deploy -native dmg -srcfiles $SOURCE_DIR -appclass $PRODUCT -name $PRODUCT -outdir $DEPLOY_DIR -outfile $PRODUCT -BappVersion=$VERSION -v

# package disk image in top-level image
mkdir -p $DSTROOT
cp deploy/bundles/NCIDpop-$VERSION.dmg $DSTROOT
cp ../../INSTALL-Mac.txt $DSTROOT
cp ../../license.txt $DSTROOT
cp ../../ReverseLookupURLs.txt $DSTROOT
cp ../../CONTRIBUTORS.txt $DSTROOT
cp ../../CHANGES.txt $DSTROOT

hdiutil create $DMG -megabytes 85 -ov -layout NONE -fs 'HFS+' -volname $VOL
MOUNT=`hdiutil attach $DMG`
DISK=`echo $MOUNT | sed -ne ' s|^/dev/\([^ ]*\).*$|\1|p'`
MOUNTPOINT=`echo $MOUNT | sed -ne 's|^.*\(/Volumes/.*\)$|\1|p'`
ditto -rsrc "$DSTROOT" "$MOUNTPOINT"
chmod -R a+rX,u+w "$MOUNTPOINT"
hdiutil detach $DISK
hdiutil resize -sectors min $DMG
hdiutil convert $DMG -format UDBZ -o z$DMG
mv z$DMG $DMG
hdiutil internet-enable $DMG
chmod 644 $DMG
