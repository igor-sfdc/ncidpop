Mac OS X: Builds with Xcode.  Last built with Xcode 6.1.1

1) Double click on addressbooklookup.xcodeproj
2) Select Product | Archive.  The project will build and the Xcode Organizer will launch
3) Click on the Export... button
4) Select “Save Built Products” and click Next
5) Ensure that the folder selection is set to NCIDpop. Also ensure that “Export As:” target is addressbooklookuplib.  Click Export.
6) Navigate to NCIDpop/addressbooklookuplib.  Locate libaddressbooklookup.dynlib.  Copy this file to 
NCIDpop/src/com/lenderman/ncidpop/resources, replacing the version that was already there 
7) You can now proceed to build NCIDpop