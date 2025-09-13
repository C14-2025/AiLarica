package br.inatel.ailarica;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeliveryTimeCalculatorTest {

    @Test //Felipe Camposssss
    @DisplayName("Deve calcular corretamente o tempo de entrega para diferentes tipos de veículo")
    public void testCalculateDeliveryTimeForDifferentVehicles() {

        double distance = 5.0;
        int bikeTime = DeliveryTimeCalculator.calculateDeliveryTime(distance,
                DeliveryTimeCalculator.DeliveryType.BIKE);
        assertEquals(35, bikeTime, "Tempo de entrega de bicicleta incorreto");

        int motorcycleTime = DeliveryTimeCalculator.calculateDeliveryTime(distance,
                DeliveryTimeCalculator.DeliveryType.MOTORCYCLE);
        assertEquals(25, motorcycleTime, "Tempo de entrega de moto incorreto");

        int carTime = DeliveryTimeCalculator.calculateDeliveryTime(distance,
                DeliveryTimeCalculator.DeliveryType.CAR);
        assertEquals(23, carTime, "Tempo de entrega de carro incorreto");

        assertTrue(carTime < motorcycleTime, "Carro deveria ser mais rápido que moto");
        assertTrue(motorcycleTime < bikeTime, "Moto deveria ser mais rápida que bicicleta");
    }
}