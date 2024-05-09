package carLocatorPkg;

import carInterfacePkg.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CarFinder {
    public static List<Car> findAllCars(){
        List<Car> cars = new ArrayList<>();
        ServiceLoader<Car> loader = ServiceLoader.load(Car.class);
        for (Car car : loader){
            cars.add(car);
        }

        // or
        //cars.addAll(loader.stream().map(ServiceLoader.Provider::get).toList());

        return cars;
    }
}
