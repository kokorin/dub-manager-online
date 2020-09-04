
# Setting up Intellij IDEA for development with Apache Royale

0. Reimport client project as Flex project (IDEA treats it as Maven because of pom.xml)

1. Download latest Flex SDK

2. Add it to list of IDEA SDKs

3. Download latest Apache Royale SDK

4. Add all SWC files from ${ROYALE_FOLDER}/royale-asjs/frameworks/js/libs as AS3 global library

5. Add the library from the previous step as client project dependency

6. On General tab in module configuration set checkbox "Skip compilation"