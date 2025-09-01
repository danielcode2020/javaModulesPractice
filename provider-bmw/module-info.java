import bmwCarImplementation.Bmw;

module carProviderImplementationBmw {
    requires carProviderInterface;
    provides carInterfacePkg.Car with Bmw;
}