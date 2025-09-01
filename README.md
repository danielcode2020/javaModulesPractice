# Project to practice modules commands for ocp certification






## Deleting .jar and .class files from current directory

find . \( -name "*.class" -o -name "*.jar" \) -delete

# 1. Interface provider module

module-info.java :

```java
module carProviderInterface {
    exports carInterfacePkg;
}
```

## Compiling the interface provider `(javac -p ... -d ... path/to/*.java)` :
```bash
  javac --module-path mods -d mods/carProviderInterface src/carProviderInterface/module-info.java src/carProviderInterface/carInterfacePkg/Car.java
```
or
```bash
 javac --module-path mods -d mods/carProviderInterface src/carProviderInterface/*.java
```

## Creating modular jar for interface provider `(jar -cvf ... -C ...)` :
```bash
jar --create --verbose --file jars/provider.jar -C mods/carProviderInterface/ .
```

# 2 Interface provider implementation module

module-info.java :

```java
import bmwCarImplementation.Bmw;

module carProviderImplementationBmw {
    requires carProviderInterface;
    provides carInterfacePkg.Car with Bmw;
}
```

## Compiling the implementation `(javac -p ... -d ... path/to/*.java)` :
```bash
  javac --module-path mods -d mods/carImplementationBMW src/carProviderImplementationBmw/module-info.java src/carProviderImplementationBmw/bmwCarImplementation/Bmw.java
```
or
```bash
 javac --module-path mods -d mods/carImplementationBMW src/carProviderImplementationBmw/*.java
```

## Creating modular jar for the implementation `(jar -cvf ... -C ...)` :

```bash
 jar --create --verbose --file jars/bmwImplementation.jar -C mods/carProviderImplementationBmw/ .
```

# 3 Service locator module

module-info.java :

```java
module carServiceLocator{
    exports carLocatorPkg;
    requires carProviderInterface;
    uses carInterfacePkg.Car;
}
```
## Compiling the service locator `(javac -p ... -d ... path/to/*.java)` :
```bash
  javac --module-path mods -d mods/carServiceLocator src/carServiceLocator/module-info.java src/carServiceLocator/carLocatorPkg/CarFinder.java
```
or
```bash
 javac --module-path mods -d mods/carServiceLocator src/carServiceLocator/*.java
```

## Creating modular jar for the service locator `(jar -cvf ... -C ...)` :
```bash
jar --create --verbose --file jars/locator.jar -C mods/carServiceLocator/ .
```

# 4 Consumer module

module-info.java
```java
module carConsumer{
    requires carServiceLocator;
    requires carProviderInterface;
}
```

## Compiling the consumer `(javac -p ... -d ... path/to/*.java)` :
```bash
  javac --module-path mods -d mods/carConsumer src/carConsumer/module-info.java src/carConsumer/consumer/Main.java
```
or
```bash
 javac --module-path mods -d mods/carConsumer src/carConsumer/*.java
```

## Creating modular jar for the consumer `(jar -cvf ... -C ...)` :
```bash
jar --create --verbose --file jars/consumer.jar -C mods/carConsumer/ .
```

# Compiling multiple modules at once `(javac & --module-source-path)` :
### This command will compile carConsumer and required modules specified in module-info : carServiceLocator, carProviderInterface)  :

* Notice that carProviderImplementationBMW will not be compiled. We should compile it separately.
```bash
javac --module-source-path src -d mods --module carConsumer
```
### Compiling all modules 
```bash
 javac --module-source-path src -d mods --module carConsumer,carProviderImplementationBmw
 ```


# Running the consumer from exploded modules directory `(java -p ... -m ...)` :

carConsumer for -m is the module name specified in module-info.class
```bash
java --module-path mods -m carConsumer/consumer.Main
```

# Building custom runtime image of java `(jlink -p ...)`
`--module-path` can take path where modular jars are located or the exploded modules directory.
It works with both.

```bash
 jlink --module-path mods --add-modules carConsumer --output carAppBin
 ```
or
```bash
 jlink --module-path jars --add-modules carConsumer --output carAppBin
```

## Running the application 
```bash
./carAppBin/bin/java --module-path mods -m carConsumer/consumer.Main
```

# Describing a module `(java/jar -d --describe-module)` :
`--describe-module` is about understanding what a module is (shows info from `module-info.java`)

### Using java
```bash
 java --module-path mods --describe-module carConsumer
```
result :
```bash
carConsumer file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carConsumer/
requires java.base mandated
requires carServiceLocator
requires carProviderInterface
contains consumer
```
or
```bash
java --module-path jars --describe-module carConsumer
```
result :
```bash
carConsumer file:///home/daniel-amd/learning/modules/javaModulesPractice/jars/consumer.jar
requires carProviderInterface
requires carServiceLocator
requires java.base mandated
contains consumer
```

### Using jdeps :

```bash
 jar --describe-module --file=jars/consumer.jar
```
result :
```bash
carConsumer jar:file:///home/daniel-amd/learning/modules/javaModulesPractice/jars/consumer.jar!/module-info.class
requires carProviderInterface
requires carServiceLocator
requires java.base mandated
contains consumer
```

# View dependencies of module (jdeps -p)

`-s --summary ` prints summary only

`-jdkinternals --jdk-internals` Finds class-level dependences in the JDK internal APIs.

 Works both with modular jars or exploded modules.
 
Example : 
```bash
jdeps --module-path jars jars/consumer.jar
```
result :
```bash

 jdeps --module-path mods mods/carConsumer 
carConsumer
 [file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carConsumer/]
   requires carProviderInterface
   requires carServiceLocator
   requires mandated java.base (@17.0.2)
carConsumer -> carServiceLocator
carConsumer -> java.base
   consumer                                           -> carLocatorPkg                                      carServiceLocator
   consumer                                           -> java.io                                            java.base
   consumer                                           -> java.lang                                          java.base
   consumer                                           -> java.util                                          java.base
   ```

# `java --show-module-resolution`
`--show-module-resolution` is about understanding how the JVM finds and links modules together to run an
application (view dependency graph). It actually executes the main class. Last 2 lines from output are from executing the class.

```bash
java --module-path mods --show-module-resolution -m carConsumer/consumer.Main
```
result :

```bash
root carConsumer file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carConsumer/
carConsumer requires carProviderInterface file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carProviderInterface/
carConsumer requires carServiceLocator file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carServiceLocator/
carServiceLocator requires carProviderInterface file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carProviderInterface/
carServiceLocator binds carProviderImplementationBmw file:///home/daniel-amd/learning/modules/javaModulesPractice/mods/carProviderImplementationBmw/
java.base binds java.management jrt:/java.management
java.base binds jdk.security.auth jrt:/jdk.security.auth
java.base binds java.desktop jrt:/java.desktop
...
jdk.editpad requires java.desktop jrt:/java.desktop
jdk.editpad requires jdk.internal.ed jrt:/jdk.internal.ed
java.rmi requires java.logging jrt:/java.logging
[Bmw 300
]
```

# `java -cp -classpath --class-path `
We can run in "old-style" jars. We can mix both modular and non-modular jars.

For example instead of :
```bash
 java --module-path jars --module carConsumer/consumer.Main
 ```
we can execute (dependencies of consumer.jar should be provided mandatory, separated by `:`) :
```bash
java -cp jars/consumer.jar:jars/locator.jar:jars/provider.jar:jars/bmwImplementation.jar consumer.Main
```

# Non modular jar commands ( gson-2.8.5.jar)

Download the jar using :
```bash
wget https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.5/gson-2.8.5.jar
```
### We can view its dependencies :

```bash
jdeps -s gson-2.8.5.jar
```
result :
```bash
gson-2.8.5.jar -> java.base
gson-2.8.5.jar -> java.sql
```

### We can describe it :

```bash
 jar --describe-module --file=gson-2.8.5.jar 
```
result (in output there is no module-info.class, therefore is non-modular) :
```bash
No module descriptor found. Derived automatic module.

gson@2.8.5 automatic
requires java.base mandated
contains com.google.gson
contains com.google.gson.annotations
contains com.google.gson.internal
``` 

# Unnamed module is the one used with -cp (on classpath)

### Dezavantaje la class path (cand rulam codul folosind class path in loc de module path)
- provides carInterfacePkg.Car with Bmw din module-info.java e ignorat
- ServiceLoader nu găsește BMW implementation
- Toate pachetele sunt accesibile (inclusiv cele interne)
- JAR hell : `java -cp gson-2.8.5.jar:gson-2.9.0.jar:app.jar MyApp` nu stim care versiune se incarca prima in runtime

### Avantaje la module path :
- Service binding funcționează (provides)
- Doar pachetele exportate sunt accesibile (asta ca sa nu facem public export la API intern)
- Dependințele sunt verificate la compile-time
- JAR hell este rezolvat la compile time deodata. In classpath speram ca in runtime se rezolva. 
Module path la compilare spune de probleme.