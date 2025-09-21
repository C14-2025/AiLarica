package br.inatel.ailarica;

public class DeliveryTimeCalculator { //Felipe Campos e reformulada por JP dia 21/09/25

    // A dependência agora é injetada pelo construtor
    private final ISpeedProvider speedProvider;
    private static final int PREPARATION_TIME = 15;

    public enum DeliveryType {
        BIKE,
        MOTORCYCLE,
        CAR
    }

    // Construtor que recebe a dependência
    public DeliveryTimeCalculator(ISpeedProvider speedProvider) {
        this.speedProvider = speedProvider;
    }

    public int calculateDeliveryTime(double distanceKm, DeliveryType deliveryType) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }
        if (deliveryType == null) {
            throw new IllegalArgumentException("Tipo de entrega não pode ser nulo");
        }

        int travelTimeMinutes = calculateTravelTime(distanceKm, deliveryType);
        return PREPARATION_TIME + travelTimeMinutes;
    }

    public int calculateTravelTime(double distanceKm, DeliveryType deliveryType) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }
        if (deliveryType == null) {
            throw new IllegalArgumentException("Tipo de entrega não pode ser nulo");
        }

        // Usa o provedor para obter a velocidade, em vez de constantes locais
        double speedKmH = speedProvider.getSpeed(deliveryType);
        if (speedKmH <= 0) {
            throw new IllegalArgumentException("A velocidade deve ser positiva.");
        }

        double travelTimeHours = distanceKm / speedKmH;
        return (int) Math.ceil(travelTimeHours * 60);
    }

    // A lógica de qual veículo é mais rápido continua a mesma
    public static DeliveryType getFastestDeliveryType(double distanceKm) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }
        if (distanceKm <= 3.0) {
            return DeliveryType.BIKE;
        } else if (distanceKm <= 10.0) {
            return DeliveryType.MOTORCYCLE;
        } else {
            return DeliveryType.CAR;
        }
    }
}