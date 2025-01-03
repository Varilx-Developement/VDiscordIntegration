package de.varilx.discordIntegration.entity;

import de.varilx.database.id.MongoId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkCode {

    @MongoId
    @Id
    UUID link;

    Long code;

    String username;


    Long timestamp;

}
