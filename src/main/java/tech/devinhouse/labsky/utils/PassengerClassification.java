package tech.devinhouse.labsky.utils;

import lombok.Getter;

@Getter
public enum PassengerClassification {
    VIP(100),
    OURO(80),
    PRATA(50),
    BRONZE(30),
    ASSOCIADO(10);

    private final Integer score;

    PassengerClassification(Integer score) {
        this.score = score;
    }
}