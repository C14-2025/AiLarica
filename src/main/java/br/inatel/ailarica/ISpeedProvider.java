package br.inatel.ailarica;

// Interface que define o contrato para quem fornece as velocidades
public interface ISpeedProvider {
    double getSpeed(DeliveryTimeCalculator.DeliveryType deliveryType);
}