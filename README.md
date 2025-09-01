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

## Compiling the interface provider :
```bash
  javac --module-path mods -d mods/carProviderInterface src/carProviderInterface/module-info.java src/carProviderInterface/carInterfacePkg/Car.java
```
or
```bash
 javac --module-path mods -d mods/carProviderInterface src/carProviderInterface/*.java
```

## Creating modular jar for interface provider :
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

## Compiling the implementation :
```bash
  javac --module-path mods -d mods/carImplementationBMW src/carProviderImplementationBmw/module-info.java src/carProviderImplementationBmw/bmwCarImplementation/Bmw.java
```
or
```bash
 javac --module-path mods -d mods/carImplementationBMW src/carProviderImplementationBmw/*.java
```

## Creating modular jar for the implementation :

```bash
jar --create --verbose --file jars/bmwImplementation.jar -C mods/carImplementationBMW/ .
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
## Compiling the service locator :
```bash
  javac --module-path mods -d mods/carServiceLocator src/carServiceLocator/module-info.java src/carServiceLocator/carLocatorPkg/CarFinder.java
```
or
```bash
 javac --module-path mods -d mods/carServiceLocator src/carServiceLocator/*.java
```

## Creating modular jar for the service locator :
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

## Compiling the consumer :
```bash
  javac --module-path mods -d mods/consumer src/carConsumer/module-info.java src/carConsumer/consumer/Main.java
```
or
```bash
 javac --module-path mods -d mods/consumer src/carConsumer/*.java
```

## Creating modular jar for the consumer :
```bash
jar --create --verbose --file jars/consumer.jar -C mods/consumer/ .
```

# Compiling multiple modules at once 
### This command will compile carConsumer and required modules specified in module-info : carServiceLocator, carProviderInterface)  :

* Notice that carProviderImplementationBMW will not be compiled. We should compile it separately.
```bash
javac --module-source-path src -d mods --module carConsumer
```


# Running the consumer from exploded modules directory (mods) :

carConsumer for -m is the module name specified in module-info.class
```bash
java --module-path mods -m carConsumer/consumer.Main
```
