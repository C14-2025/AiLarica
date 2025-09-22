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
}
