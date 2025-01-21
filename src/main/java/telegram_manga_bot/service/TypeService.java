package telegram_manga_bot.service;

import com.bot.telegram_manga_bot.repository.TypeRepository;
import com.bot.telegram_manga_bot.repository.entity.MangaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }


    public Long determineType(String typeOfManga) {
        MangaType existedType = typeRepository.getByType(typeOfManga.toLowerCase());
        List<MangaType> existingTypes = typeRepository.findAll();

        if (existingTypes.contains(existedType)){
            return existedType.getId();
        }else {
            MangaType newType = typeRepository.save(new MangaType(typeOfManga.toLowerCase()));
            return newType.getId();
        }


    }
}