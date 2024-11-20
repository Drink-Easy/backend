<<<<<<<< HEAD:src/main/java/com/drinkeg/drinkeg/repository/wineNote/WineNoteRepository.java
package com.drinkeg.drinkeg.repository.wineNote;
========
package com.drinkeg.drinkeg.wineNote.repository;
>>>>>>>> dev:src/main/java/com/drinkeg/drinkeg/wineNote/repository/WineNoteRepository.java

import com.drinkeg.drinkeg.wineNote.domain.WineNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNote, Long>, WineNoteRepositoryCustom {

}