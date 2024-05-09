all operations in /ModulesPractice $:

1 service provider interface


javac -d carProviderInterface carProviderInterface/carInterfacePkg/*.java carProviderInterface/module-info.java

jar -cvf aTemp/provider.jar -C carProviderInterface/ .


2 service locator

javac -p aTemp -d carServiceLocator carServiceLocator/carLocatorPkg/*.java carServiceLocator/module-info.java

jar -cvf aTemp/carLocator.jar -C carServiceLocator/ .


3 consumer

javac -p aTemp -d carConsumer carConsumer/consumer/*.java carConsumer/module-info.java

jar -cvf aTemp/carConsumer.jar -C carConsumer/ .

java -p aTemp -m carConsumer/consumer.Main

4 another implementation and calling consumer

javac -p aTemp -d carProviderImplementationBmw carProviderImplementationBmw/bmwCarImplementation/*.java carProviderImplementationBmw/module-info.java

jar -cvf aTemp/carBmwImplementation.jar -C carProviderImplementationBmw/ .

and now we should get another implementation :  java -p aTemp -m carConsumer/consumer.Main

best part is that we simply added another implementation and everything worked without recompiling any provider, locator or anything
 