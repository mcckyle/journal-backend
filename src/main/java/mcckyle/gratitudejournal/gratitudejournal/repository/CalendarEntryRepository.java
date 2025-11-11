//***************************************************************************************
//
//     Filename: CalendarEntryRepository.java
//     Author: Kyle McColgan
//     Date: 10 November 2025
//     Description: This file provides functionality for CalendarEntry-related functionality.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.repository;

import mcckyle.gratitudejournal.gratitudejournal.model.CalendarEntry;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

public interface CalendarEntryRepository extends CrudRepository<CalendarEntry, Integer>
{
    List<CalendarEntry> findByUserId(Integer userID);
    Optional<CalendarEntry> findByUserIdAndId(Integer userId, Integer id);
}

//***************************************************************************************
