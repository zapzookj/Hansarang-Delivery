package com.hansarangdelivery.address.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hansarangdelivery.global.model.TimeStamped;
import com.hansarangdelivery.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_delivery_address")
@SQLDelete(sql = "UPDATE p_deliveryAddress SET deleted_at=CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
public class DeliveryAddress extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "location_id", nullable = false)
    private UUID locationId;  // Location의 ID만 저장

    @Column(name = "request_message")
    private String requestMessage;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    public DeliveryAddress(User user, UUID locationId, String requestMessage, boolean isDefault) {
        this.user = user;
        this.locationId = locationId;
        this.requestMessage = requestMessage;
        this.isDefault = isDefault;
    }

    public void update(UUID locationId, String requestMessage, Boolean isDefault) {
        this.locationId = locationId;
        this.requestMessage = requestMessage;
        this.isDefault = isDefault;
    }
}

