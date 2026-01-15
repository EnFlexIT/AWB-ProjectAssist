## Setting Version 
Different to a regular Maven bundle, you need to edit two entries to update or increase the version of the AWB-Assist core bundle.  
The correspondent files to edit are:
- **pom.xml**: This is the regular way for a Maven project
- **src/main/resources/META-INF/MANIFEST.MF**: this is MANIFEST that will be exported to the finally produced OSGI artifact.

Further, for the AWB-Tools, the new version needs to be used in the target platform definition.