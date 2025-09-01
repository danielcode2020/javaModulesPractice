package bmwCarImplementation;

import carInterfacePkg.Car;

public class Bmw implements Car {
    @Override
    public int getSpeed() {
        return 300;
    }

    @Override
    public String getName() {
        return "Bmw";
    }
}
