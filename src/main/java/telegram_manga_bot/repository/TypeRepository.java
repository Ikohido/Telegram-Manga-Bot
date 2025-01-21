package telegram_manga_bot.repository;

import com.bot.telegram_manga_bot.repository.entity.MangaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<MangaType, Long> {
    MangaType getByType(String type);
}
