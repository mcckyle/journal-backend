//***************************************************************************************
//
//     Filename: CalendarEntryController.java
//     Author: Kyle McColgan
//     Date: 1 December 2025
//     Description: This controller class provides CalendarEntry functionality.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.controller;

import jakarta.validation.Valid;
import mcckyle.gratitudejournal.gratitudejournal.dto.CalendarEntryDTO;
import mcckyle.gratitudejournal.gratitudejournal.model.CalendarEntry;
import mcckyle.gratitudejournal.gratitudejournal.security.UserDetailsImpl;
import mcckyle.gratitudejournal.gratitudejournal.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

@RestController
@RequestMapping("/api/calendar") // Base URL for calendar entry endpoints.
public class CalendarEntryController
{
    private final CalendarService calendarService;

    @Autowired
    public CalendarEntryController(CalendarService calendarService)
    {
        this.calendarService = calendarService;
    }

    // GET all calendar entries for a specific user.
    @GetMapping
    public ResponseEntity<List<CalendarEntry>> getEntries(Authentication authentication)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Integer userId = userDetails.getId();

        List<CalendarEntry> entries = calendarService.getEntriesByUserId(userId);

        if ( (entries == null) || (entries.isEmpty()) )
        {
            return ResponseEntity.noContent().build(); // 204 - No Content.
        }

        return ResponseEntity.ok(entries); // 200 - OK with the list of entries.
    }

    // Get a calendar entry by ID.
    @GetMapping("/{entryId}")
    public ResponseEntity<?> getEntryById(Authentication authentication, @PathVariable Integer entryId)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Integer userId = userDetails.getId();

        Optional<CalendarEntry> entry = calendarService.getEntryById(userId, entryId);  // Pass both userId and entryId to the service method.

        if (entry.isPresent())
        {
            // If the entry exists, return it with a 200 - OK response.
            return ResponseEntity.ok(entry.get());
        }
        else
        {
            // If the entry is not found, return a 404 - NOT FOUND response.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calendar entry not found");
        }
    }

    // POST (create) a new calendar entry.
    @PostMapping
    public ResponseEntity<CalendarEntry> createEntry(Authentication authentication, @Valid @RequestBody CalendarEntryDTO dto)
    {
        try
        {
            //FIRST: validate DTO (test #9 depends on this).
            if ( (dto == null) || (dto.entryDate == null) )
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //THEN: validate authentication.
            if ( (authentication == null) || ( ! authentication.isAuthenticated()) )
            {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Integer userId = userDetails.getId();

            LocalDate date = LocalDate.parse(dto.entryDate); //Convert the String to LocalDate.

            CalendarEntry entry = new CalendarEntry(dto.getTitle(), dto.getContent(), date, userId);
            CalendarEntry createdEntry = calendarService.createEntry(entry);

            return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT (update) an existing calendar entry.
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEntry> updateEntry(Authentication authentication, @PathVariable Integer id, @Valid @RequestBody CalendarEntryDTO dto)
    {
        try
        {
            if ( (authentication == null) || ( ! authentication.isAuthenticated()) )
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Integer userId = userDetails.getId();

            LocalDate date = LocalDate.parse(dto.entryDate); //Convert the String to LocalDate.
            CalendarEntry entry = new CalendarEntry(dto.getTitle(), dto.getContent(), date, userId);
            CalendarEntry updatedEntry = calendarService.updateEntry(userId, id, entry);
            return updatedEntry != null ? ResponseEntity.ok(updatedEntry) : ResponseEntity.notFound().build();
        }
        catch (RuntimeException e)
        {
            // Handle unexpected errors.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE a calendar entry by ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(Authentication authentication, @PathVariable("id") Integer entryId)
    {
        try
        {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Integer userId = userDetails.getId();

            calendarService.deleteEntry(userId, entryId);  // Try to delete the entry.
            return ResponseEntity.noContent().build();  // Return 204 - No Content if successful.
        }
        catch (ResponseStatusException e)
        {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                return ResponseEntity.notFound().build();  // Return 404 - NOT FOUND response if entry not found.
            }
            throw e;  // Rethrow any other unexpected exceptions.
        }
    }
}

//***************************************************************************************

