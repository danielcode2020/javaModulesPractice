package consumer;

import carInterfacePkg.Car;
import carLocatorPkg.CarFinder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Car> carList = CarFinder.findAllCars();
        System.out.println(carList);

    }
}
