//***************************************************************************************
//
//   Filename: CalendarService.java
//   Author: Kyle McColgan
//   Date: 10 November 2025
//   Description: This file provides abstracted functionality for the CalendarEntry.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.service;

import mcckyle.gratitudejournal.gratitudejournal.model.CalendarEntry;
import mcckyle.gratitudejournal.gratitudejournal.repository.CalendarEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

@Service
public class CalendarService
{
    private final CalendarEntryRepository calendarEntryRepository;

    @Autowired
    public CalendarService(CalendarEntryRepository calendarEntryRepository)
    {
        this.calendarEntryRepository = calendarEntryRepository;
    }

    public List<CalendarEntry> getEntriesByUserId(Integer userId)
    {
        return calendarEntryRepository.findByUserId(userId);
    }

    public CalendarEntry createEntry(CalendarEntry entry)
    {
        if (entry.getTitle() == null || entry.getTitle().trim().isEmpty())
        {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (entry.getContent() == null || entry.getContent().trim().isEmpty())
        {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (entry.getEntryDate() == null)
        {
            throw new IllegalArgumentException("Entry date cannot be null");
        }
        return calendarEntryRepository.save(entry);
    }

    public CalendarEntry updateEntry(Integer id, CalendarEntry entry)
    {
        // Check if the entry exists
        Optional<CalendarEntry> existingEntry = calendarEntryRepository.findById(id);
        if (existingEntry.isPresent())
        {
            CalendarEntry entryToUpdate = existingEntry.get();

            // Update the entry fields
            entryToUpdate.setTitle(entry.getTitle());
            entryToUpdate.setContent(entry.getContent());

            // Save and return the updated entry
            return calendarEntryRepository.save(entryToUpdate);
        }
        return null;  // Return null if entry not found
    }

    public Optional<CalendarEntry> getEntryById(Integer userId, Integer entryId)
    {
        // Assuming `findByUserIdAndEntryId` is a method that finds an entry by both userId and entryId.
        return calendarEntryRepository.findByUserIdAndId(userId, entryId);
    }

    public void deleteEntry (Integer id)
    {
        if (!calendarEntryRepository.existsById(id))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendar entry not found");
        }
        calendarEntryRepository.deleteById(id);
    }
}

//***************************************************************************************

