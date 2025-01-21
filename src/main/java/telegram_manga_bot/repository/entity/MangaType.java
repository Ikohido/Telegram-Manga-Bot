package telegram_manga_bot.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "types")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MangaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    @EqualsAndHashCode.Exclude
    private String type;

    public MangaType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MangaTypes{" +
                "type='" + type + '\'' +
                '}';
    }
}
