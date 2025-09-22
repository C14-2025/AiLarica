package br.inatel.ailarica;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryTimeCalculatorMockTest {

    @Test
    void deveLancarExcecaoQuandoVelocidadeForZero() {
        // Cria um mock do ISpeedProvider
        ISpeedProvider speedProviderMock = Mockito.mock(ISpeedProvider.class);

        // Configura para retornar 0 km/h para qualquer tipo de entrega
        when(speedProviderMock.getSpeed(any())).thenReturn(0.0);

        DeliveryTimeCalculator calculator = new DeliveryTimeCalculator(speedProviderMock);

        // Verifica que uma exceção é lançada
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTravelTime(5.0, DeliveryTimeCalculator.DeliveryType.CAR)
        );

        assertEquals("A velocidade deve ser positiva.", ex.getMessage());
        verify(speedProviderMock, times(1)).getSpeed(DeliveryTimeCalculator.DeliveryType.CAR);
    }

    @Test
    void testCalculateDeliveryTimeComBicicleta() {
        ISpeedProvider speedProviderMock = Mockito.mock(ISpeedProvider.class);

        when(speedProviderMock.getSpeed(DeliveryTimeCalculator.DeliveryType.BIKE)).thenReturn(15.0);
        DeliveryTimeCalculator calculator = new DeliveryTimeCalculator(speedProviderMock);

        int resultado = calculator.calculateDeliveryTime(5.0, DeliveryTimeCalculator.DeliveryType.BIKE);

        assertEquals(35, resultado); // 15 minutos de preparo + 20 minutos de viagem (5km / 15km/h = 0.33h = 20min)
        verify(speedProviderMock, times(1)).getSpeed(DeliveryTimeCalculator.DeliveryType.BIKE);
    }

    @Test
    void testCalculateDeliveryTimeComMotocicleta() {
        ISpeedProvider speedProviderMock = Mockito.mock(ISpeedProvider.class);

        when(speedProviderMock.getSpeed(DeliveryTimeCalculator.DeliveryType.MOTORCYCLE)).thenReturn(40.0);
        DeliveryTimeCalculator calculator = new DeliveryTimeCalculator(speedProviderMock);

        int resultado = calculator.calculateDeliveryTime(10.0, DeliveryTimeCalculator.DeliveryType.MOTORCYCLE);

        assertEquals(30, resultado); // 15 minutos de preparo + 15 minutos de viagem (10km / 40km/h = 0.25h = 15min)
        verify(speedProviderMock, times(1)).getSpeed(DeliveryTimeCalculator.DeliveryType.MOTORCYCLE);
    }

    @Test
    void testCalculateDeliveryTimeComCarro() {
        ISpeedProvider speedProviderMock = Mockito.mock(ISpeedProvider.class);

        when(speedProviderMock.getSpeed(DeliveryTimeCalculator.DeliveryType.CAR)).thenReturn(60.0);
        DeliveryTimeCalculator calculator = new DeliveryTimeCalculator(speedProviderMock);

        int resultado = calculator.calculateDeliveryTime(20.0, DeliveryTimeCalculator.DeliveryType.CAR);

        assertEquals(35, resultado); // 15 minutos de preparo + 20 minutos de viagem (20km / 60km/h = 0.33h = 20min)
        verify(speedProviderMock, times(1)).getSpeed(DeliveryTimeCalculator.DeliveryType.CAR);
    }
}
