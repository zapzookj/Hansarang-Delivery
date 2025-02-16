package com.hansarangdelivery.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
<<<<<<< Updated upstream
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
=======

>>>>>>> Stashed changes
public enum OrderType {
    ONLINE("온라인 주문"),
    OFFLINE("대면 주문");

    private final String description;

<<<<<<< Updated upstream
}

=======
    OrderType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static OrderType fromString(String value) {
        for (OrderType type : OrderType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown OrderType: " + value);
    }
}
>>>>>>> Stashed changes
