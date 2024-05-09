module carServiceLocator{
    exports carLocatorPkg;
    requires carProviderInterface;
    uses carInterfacePkg.Car;
}