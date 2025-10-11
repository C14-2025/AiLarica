package br.inatel.ailarica;

public class DeliveryCostCalculator {
    private static final double CUSTO_BASE_BIKE = 5.0;
    private static final double CUSTO_POR_KM_BIKE = 1.5;
    private static final double CUSTO_BASE_MOTORCYCLE = 7.0;
    private static final double CUSTO_POR_KM_MOTORCYCLE = 2.0;
    private static final double CUSTO_BASE_CAR = 10.0;
    private static final double CUSTO_POR_KM_CAR = 2.5;

    public double calcularCustoEntrega(double distanciaKm, DeliveryTimeCalculator.DeliveryType tipoEntrega) {
        if (distanciaKm < 0) {
            throw new IllegalArgumentException("A distância não pode ser negativa");
        }
        if (tipoEntrega == null) {
            throw new IllegalArgumentException("Tipo de entrega não pode ser nulo");
        }

        double custoTotal = 0;

        switch (tipoEntrega) {
            case BIKE:
                custoTotal = CUSTO_BASE_BIKE + (distanciaKm * CUSTO_POR_KM_BIKE);
                break;
            case MOTORCYCLE:
                custoTotal = CUSTO_BASE_MOTORCYCLE + (distanciaKm * CUSTO_POR_KM_MOTORCYCLE);
                break;
            case CAR:
                custoTotal = CUSTO_BASE_CAR + (distanciaKm * CUSTO_POR_KM_CAR);
                break;
            default:
                throw new IllegalArgumentException("Tipo de entrega desconhecido");
        }
        return custoTotal;
    }
}

