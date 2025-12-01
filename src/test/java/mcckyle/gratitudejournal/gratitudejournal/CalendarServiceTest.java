//***************************************************************************************
//
//     Filename: CalendarServiceTest.java
//     Author: Kyle McColgan
//     Date: 30 November 2025
//     Description: This file contains unit tests for the CalendarService.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal;

import mcckyle.gratitudejournal.gratitudejournal.model.CalendarEntry;
import mcckyle.gratitudejournal.gratitudejournal.repository.CalendarEntryRepository;
import mcckyle.gratitudejournal.gratitudejournal.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest
{
    @Mock
    private CalendarEntryRepository repository;

    @InjectMocks
    private CalendarService service;

    private CalendarEntry sampleEntry;

    @BeforeEach
    void setUp()
    {
        sampleEntry = new CalendarEntry();
        sampleEntry.setId(101);
        sampleEntry.setUserId(1);
        sampleEntry.setTitle("Sample Title");
        sampleEntry.setContent("Sample Content");
        sampleEntry.setEntryDate(LocalDate.now());
    }

    // Test 1: Service - Find entry by ID
    @Test
    void testFindEntryById()
    {
        // Arrange: mock the repository to return an Optional with a sample entry
        when(repository.findByUserIdAndId(1, 101)).thenReturn(Optional.of(sampleEntry));

        // Act: call the service method that now takes both userId and entryId as parameters
        Optional<CalendarEntry> result = service.getEntryById(1, 101);

        // Assert: check that the Optional contains the expected entry and validate its properties
        assertThat(result).isPresent(); // Assert that the Optional is not empty
        assertThat(result.get().getTitle()).isEqualTo("Sample Title"); // Assert the title of the found entry

        // Verify: check if the repository's findByUserIdAndEntryId method was called with the correct arguments
        verify(repository).findByUserIdAndId(1, 101);
    }

    // Test 2: Service - Save new entry
    @Test
    void testSaveEntry()
    {
        when(repository.save(sampleEntry)).thenReturn(sampleEntry);
        CalendarEntry savedEntry = service.createEntry(sampleEntry);
        assertThat(savedEntry).isNotNull();
        verify(repository).save(sampleEntry);
    }

    // Test 3: Service - Get all entries for a user
    @Test
    void testGetEntriesByUserId()
    {
        List<CalendarEntry> entries = new ArrayList<>();
        entries.add(sampleEntry);
        when(repository.findByUserId(123)).thenReturn(entries);
        List<CalendarEntry> result = service.getEntriesByUserId(123);
        assertThat(result).hasSize(1);
    }

    // Test 4: Service - Delete entry
    @Test
    void testDeleteEntry()
    {
        // Mock findByUserIDAndId to return a sample entry.
        when(repository.findByUserIdAndId(1,1)).thenReturn(Optional.of(sampleEntry));

        // Call the deleteEntry() method.
        service.deleteEntry(1, 1);

        //Verify repository methods are called with the correct parameters.
        verify(repository).findByUserIdAndId(1, 1);  // Ensure existsById was invoked.
        verify(repository).delete(sampleEntry);  // Ensure deleteById was invoked.
    }

    // Test 5: Service - Update entry
    @Test
    void testUpdateEntry()
    {
        CalendarEntry updatedEntry = new CalendarEntry();
        updatedEntry.setId(1);
        updatedEntry.setTitle("Updated Title");
        updatedEntry.setContent("Updated Content");

        when(repository.findByUserIdAndId(1, 101)).thenReturn(Optional.of(sampleEntry));
        when(repository.save(sampleEntry)).thenReturn(sampleEntry);

        CalendarEntry result = service.updateEntry(1, 101, updatedEntry);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getContent()).isEqualTo("Updated Content");

        verify(repository).findByUserIdAndId(1, 101);
        verify(repository).save(sampleEntry);
    }

    // Test 6: Edge Case - Save entry with null fields
    @Test
    void testSaveEntryWithNullFields()
    {
        CalendarEntry invalidEntry = new CalendarEntry(); // Missing title/content
        assertThatThrownBy(() -> service.createEntry(invalidEntry))
                .isInstanceOf(IllegalArgumentException.class); // Custom validation needed
    }

    //Test #7: Update Non-existent Entry. - Validate That Updating a Non-existent Entry Returns Null.
    @Test
    void testUpdateNonExistentEntry()
    {
        CalendarEntry updatedEntry = new CalendarEntry();
        updatedEntry.setTitle("Non-existent");
        updatedEntry.setContent("Content");

        when(repository.findByUserIdAndId(999, 101)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateEntry(999, 101, updatedEntry))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Calendar entry not found");

        verify(repository).findByUserIdAndId(999, 101);
    }

    //Test #8: Delete Non-existent Entry. - Ensure That Attempting to Delete
    // an Entry That Does Not Exist Throws A ResponseStatusException.
    @Test
    void testDeleteNonExistentEntry()
    {
        //Mock the findByUserIdAndId() to return empty.
        when(repository.findByUserIdAndId(999, 101)).thenReturn(Optional.empty());

        //Assert the exception.
        assertThatThrownBy(() -> service.deleteEntry(999, 101))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Calendar entry not found");

        verify(repository).findByUserIdAndId(999, 101);
    }

    //Test #9: Get Entries For User With No Entries. -
    //Verify That Retrieving Entries For a User With No Entries
    //Returns an Empty List.
    @Test
    void testGetEntriesByUserIdNoEntries()
    {
        when(repository.findByUserId(2)).thenReturn(new ArrayList<>());

        List<CalendarEntry> result = service.getEntriesByUserId(2);

        assertThat(result).isEmpty();
        verify(repository).findByUserId(2);
    }

    //Test #10: Create Entry With Future Date. - Ensure That Creating
    //An Entry With a Future Date Works Correctly.
    @Test
    void testCreateEntryWithFutureDate()
    {
        CalendarEntry futureEntry = new CalendarEntry();
        futureEntry.setTitle("Future Title");
        futureEntry.setContent("Future Content");
        futureEntry.setEntryDate(LocalDate.now().plusDays(10));

        when(repository.save(futureEntry)).thenReturn(futureEntry);

        CalendarEntry result = service.createEntry(futureEntry);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Future Title");
        assertThat(result.getEntryDate()).isAfter(LocalDate.now());

        verify(repository).save(futureEntry);
    }
}
