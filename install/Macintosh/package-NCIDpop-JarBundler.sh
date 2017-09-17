#!/bin/zsh -e

PACKAGEDIR="$PWD"
PRODUCT="NCIDpop"

# gather information
VERSION=$(/usr/libexec/PlistBuddy -c 'Print :CFBundleVersion' NCIDpop.app/Contents/Info.plist)
DMG="$PRODUCT-MacOSX-Compat-$VERSION.dmg" VOL="$PRODUCT $VERSION"
DSTROOT="$PACKAGEDIR/$VOL"

# clean
sudo rm -rf "$DSTROOT"

# copy built files.  Should have been built using eclipse
mkdir -p $DSTROOT
cp -r NCIDpop.app $DSTROOT
cp ../../INSTALL-Mac.txt $DSTROOT
cp ../../license.txt $DSTROOT
cp ../../ReverseLookupURLs.txt $DSTROOT
cp ../../CONTRIBUTORS.txt $DSTROOT
cp ../../CHANGES.txt $DSTROOT

# create disk image
cd "$PACKAGEDIR"
rm -f $DMG
hdiutil create $DMG -megabytes 7 -ov -layout NONE -fs 'HFS+' -volname $VOL
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
