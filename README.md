# mp - Maven Package in CLI, written in Kotlin

mp can search Maven Package. Now supporting Maven Central and JCenter. (Pull Request is always welcome!)  
mp is written in Kotlin and build by GraalVM.

## build and install

```shell script
git clone https://github.com/hiroga-cc/mp
cd mp
./gradlew native-image
cp ./bin/mp /usr/local/bin/mp
```

In the future, mp will support homebrew.  

## usage

```shell script
mp search ktor

Central (https://repo1.maven.org/maven2/)

GroupId                                  ArtifactId                       LatestVersion
--------------------------------------------------------------------------------
com.bnorm.ktor.retrofit                  ktor-retrofit                    0.1.2
com.cedarsoft.dependencies-sets          ktor                             8.9.2
com.cedarsoft.tests                      ktor                             8.7.0
com.github.rarnu                         ktor                             0.2.0
io.ktor                                  binary-compatibility-validator-kotlinMultiplatform 1.1.5
io.ktor                                  ktor                             1.3.1
io.ktor                                  ktor-auth-jwt-kotlinMultiplatform 1.3.1
io.ktor                                  ktor-auth-kotlinMultiplatform    1.3.1
.........

Bintray's JCenter (https://jcenter.bintray.com/)

GroupId                                  ArtifactId                       LatestVersion
--------------------------------------------------------------------------------
.........
io.ktor                                  ktor-websockets                  1.0.0-drew-1
io.ktor                                  ktor-websockets-kotlinMultiplatform 1.2.5-1.3.60-eap-76
io.ktor                                  ktor-websockets-metadata         1.2.5-1.3.60-eap-76
.........
```
