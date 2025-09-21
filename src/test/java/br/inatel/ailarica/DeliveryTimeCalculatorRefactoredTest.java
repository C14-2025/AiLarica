package br.inatel.ailarica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryTimeCalculatorRefactoredTest {

    @Mock // Cria um objeto falso (mock) da nossa dependência
    private ISpeedProvider mockSpeedProvider;

    private DeliveryTimeCalculator calculator;

    @BeforeEach
    void setUp() {
        // Instancia a classe que queremos testar, injetando o mock no construtor
        calculator = new DeliveryTimeCalculator(mockSpeedProvider);
    }

    @Test
    @DisplayName("Deve calcular o tempo de entrega corretamente com velocidades customizadas do mock")
    public void testCalculateDeliveryTimeComVelocidadesDoMock() {
        // Arrange (Organização):
        // Dizemos ao mock como ele deve se comportar.
        // "Quando o método getSpeed for chamado com CAR, então retorne 50.0 km/h"
        when(mockSpeedProvider.getSpeed(DeliveryTimeCalculator.DeliveryType.CAR)).thenReturn(50.0);

        // Act (Ação):
        // Executamos o método a ser testado. A distância é 10km.
        int deliveryTime = calculator.calculateDeliveryTime(10.0, DeliveryTimeCalculator.DeliveryType.CAR);

        // Assert (Verificação):
        // Verificamos se o resultado está correto, baseado na velocidade que o mock forneceu.
        // Cálculo esperado: (10km / 50km/h) * 60 min/h = 12 minutos de viagem.
        // Tempo total = 15 (preparo) + 12 (viagem) = 27 minutos.
        assertEquals(27, deliveryTime);
    }

    @Test
    @DisplayName("Deve calcular o tempo de entrega para moto em um cenário de trânsito")
    public void testCalculateDeliveryTimeParaMotoComTransito() {
        // Arrange:
        // Simulamos um cenário diferente: uma moto andando devagar (trânsito).
        when(mockSpeedProvider.getSpeed(DeliveryTimeCalculator.DeliveryType.MOTORCYCLE)).thenReturn(15.0); // Velocidade de bike

        // Act:
        int deliveryTime = calculator.calculateDeliveryTime(5.0, DeliveryTimeCalculator.DeliveryType.MOTORCYCLE);

        // Assert:
        // Cálculo esperado: (5km / 15km/h) * 60 min/h = 20 minutos de viagem.
        // Tempo total = 15 (preparo) + 20 (viagem) = 35 minutos.
        assertEquals(35, deliveryTime);
    }
}
