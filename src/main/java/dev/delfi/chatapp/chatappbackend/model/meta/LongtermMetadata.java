package dev.delfi.chatapp.chatappbackend.model.meta;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LongtermMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("deleted_user_count")
    private Long deletedUserCount;

    @JsonProperty("name")
    @Column(unique = true, nullable = false)
    private String name;

    public LongtermMetadata(Long deletedUserCount, String name) {
        this.deletedUserCount = deletedUserCount;
        this.name = name;
    }
}
