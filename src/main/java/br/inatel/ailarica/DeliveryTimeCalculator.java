package br.inatel.ailarica;

public class DeliveryTimeCalculator { //Felipe Campos

    private static final double BIKE_SPEED_KM_H = 15.0;
    private static final double MOTORCYCLE_SPEED_KM_H = 30.0;
    private static final double CAR_SPEED_KM_H = 40.0;

    private static final int PREPARATION_TIME = 15;

    public enum DeliveryType {
        BIKE,
        MOTORCYCLE,
        CAR
    }

    public static int calculateDeliveryTime(double distanceKm, DeliveryType deliveryType) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }

        if (deliveryType == null) {
            throw new IllegalArgumentException("Tipo de entrega não pode ser nulo");
        }

        double speedKmH = getSpeedByDeliveryType(deliveryType);
        double travelTimeHours = distanceKm / speedKmH;
        int travelTimeMinutes = (int) Math.ceil(travelTimeHours * 60);

        return PREPARATION_TIME + travelTimeMinutes;
    }

    public static int calculateTravelTime(double distanceKm, DeliveryType deliveryType) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }

        if (deliveryType == null) {
            throw new IllegalArgumentException("Tipo de entrega não pode ser nulo");
        }

        double speedKmH = getSpeedByDeliveryType(deliveryType);
        double travelTimeHours = distanceKm / speedKmH;

        return (int) Math.ceil(travelTimeHours * 60);
    }

    private static double getSpeedByDeliveryType(DeliveryType deliveryType) {
        switch (deliveryType) {
            case BIKE:
                return BIKE_SPEED_KM_H;
            case MOTORCYCLE:
                return MOTORCYCLE_SPEED_KM_H;
            case CAR:
                return CAR_SPEED_KM_H;
            default:
                throw new IllegalArgumentException("Tipo de entrega não suportado: " + deliveryType);
        }
    }

    public static DeliveryType getFastestDeliveryType(double distanceKm) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }

        if (distanceKm <= 3.0) {
            return DeliveryType.BIKE;
        }
        else if (distanceKm <= 10.0) {
            return DeliveryType.MOTORCYCLE;
        }
        else {
            return DeliveryType.CAR;
        }
    }
}