package telegram_manga_bot.repository;

import com.bot.telegram_manga_bot.repository.entity.MangaEntity;
import com.bot.telegram_manga_bot.repository.entity.MangaGenre;
import jakarta.validation.constraints.Max;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface MangaEntityRepository extends JpaRepository<MangaEntity, Integer> {
    List<MangaEntity> getByGenresIn(List<MangaGenre> genres);

    @Query(value = "SELECT * FROM manga ORDER BY random() LIMIT 1", nativeQuery = true)
    Optional<MangaEntity> getRandomManga();

    @Query(value = "SELECT * FROM manga WHERE manga.created_at >= :timestamp", nativeQuery = true)
    List<MangaEntity> getMangaEntitiesByCreatedAt(@Param("timestamp") Timestamp timestamp);

    @Query(value = "SELECT * FROM manga ORDER BY id LIMIT :count", nativeQuery = true)
    List<MangaEntity> getMangaByBeginning(@Param("count") @Max(20) int count);

    @Query(value = "SELECT * FROM manga ORDER BY id DESC LIMIT :count", nativeQuery = true)
    List<MangaEntity> getMangaByEnd(@Param("count") @Max(20) int count);

    Optional<MangaEntity> getMangaEntityByRusNameAndImgUrl(String rusName, String coverUrl);

    Optional<MangaEntity> getById(long id);

    @Query(value = "SELECT * FROM manga", nativeQuery = true)
    List<MangaEntity> getAll();
}
