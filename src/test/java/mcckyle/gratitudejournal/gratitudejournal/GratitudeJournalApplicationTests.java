//***************************************************************************************
//
//     Filename: GratitudeJournalApplicationTests.java
//     Author: Kyle McColgan
//     Date: 11 November 2025
//     Description: This file contains unit tests for the gratitude journal back end.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal;

import mcckyle.gratitudejournal.gratitudejournal.controller.CalendarEntryController;
import mcckyle.gratitudejournal.gratitudejournal.model.CalendarEntry;
import mcckyle.gratitudejournal.gratitudejournal.repository.CalendarEntryRepository;
import mcckyle.gratitudejournal.gratitudejournal.service.CalendarService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

//***************************************************************************************

@ExtendWith(MockitoExtension.class) // Replaces @SpringBootTest for lightweight testing
class GratitudeJournalApplicationTests
{
	//Inject mocks for controllers and services, just mock annotation for the repository.
	@InjectMocks
	private CalendarEntryController controller;  // Inject the mocked service into the controller

	@InjectMocks
	private CalendarService service;  // Mock the CalendarService

	@Mock
	private CalendarEntryRepository repository;  // Keep the repository mock if used in service tests

	private CalendarEntry sampleEntry;

	@BeforeEach
	void setUp()
	{
		sampleEntry = new CalendarEntry();
		sampleEntry.setId(1);
		sampleEntry.setTitle("Sample Title");
		sampleEntry.setContent("Sample Content");
		sampleEntry.setEntryDate(LocalDate.now());
		sampleEntry.setUserId(123);
	}

	// Test 1: Context loads
	@Test
	void contextLoads()
	{
		assertThat(controller).isNotNull();  // Only the controller needs to be checked here
	}

	// Test 2: Service - Find entry by ID
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

	// Test 3: Service - Save new entry
	@Test
	void testSaveEntry()
	{
		when(repository.save(sampleEntry)).thenReturn(sampleEntry);
		CalendarEntry savedEntry = service.createEntry(sampleEntry);
		assertThat(savedEntry).isNotNull();
		verify(repository).save(sampleEntry);
	}

	// Test 4: Service - Get all entries for a user
	@Test
	void testGetEntriesByUserId()
	{
		List<CalendarEntry> entries = new ArrayList<>();
		entries.add(sampleEntry);
		when(repository.findByUserId(123)).thenReturn(entries);
		List<CalendarEntry> result = service.getEntriesByUserId(123);
		assertThat(result).hasSize(1);
	}

	// Test 5: Service - Delete entry
	@Test
	void testDeleteEntry()
	{
		// Mock repository.existsById to return true
		when(repository.existsById(1)).thenReturn(true);

		// Call deleteEntry method
		service.deleteEntry(1);

		// Verify repository methods are called with the correct parameters
		verify(repository).existsById(1);  // Ensure existsById was invoked
		verify(repository).deleteById(1);  // Ensure deleteById was invoked
	}

	// Test 6: Service - Update entry
	@Test
	void testUpdateEntry()
	{
		CalendarEntry updatedEntry = new CalendarEntry();
		updatedEntry.setId(1);
		updatedEntry.setTitle("Updated Title");
		updatedEntry.setContent("Updated Content");

		when(repository.findById(1)).thenReturn(Optional.of(sampleEntry));
		when(repository.save(any(CalendarEntry.class))).thenReturn(updatedEntry);

		CalendarEntry result = service.updateEntry(1, updatedEntry);
		assertThat(result.getTitle()).isEqualTo("Updated Title");
	}

	// Test 7: Repository - Entry not found
	@Test
	void testEntryNotFound()
	{
		// Arrange: Mock the repository to return an empty Optional when looking for a non-existent entry
		when(repository.findByUserIdAndId(2, 999)).thenReturn(Optional.empty());

		// Act: Call the service method to get the entry by user ID and entry ID
		Optional<CalendarEntry> result = service.getEntryById(2, 999); // Pass both userId and entryId

		// Assert: Check that the result is empty (i.e., no entry found)
		assertThat(result).isEmpty();

		// Verify: Ensure the repository method was called with the correct parameters
		verify(repository).findByUserIdAndId(2, 999);
	}

	// Test 8: test find ID
	@Test
	void testFindID()
	{
		// Arrange: Simulate that no entry exists with userId = 2 and entryId = 999
		when(repository.findByUserIdAndId(2, 999)).thenReturn(Optional.empty());

		// Act: Call the service method to get an entry by userId and entryId
		Optional<CalendarEntry> result = service.getEntryById(2, 999);  // Pass both userId and entryId

		// Verify: Ensure the repository's findByUserIdAndEntryId was called
		verify(repository).findByUserIdAndId(2, 999);

		// Assert: Verify the result is empty, as no entry should be found
		assertThat(result).isEmpty();
	}

	// Test 9: Edge Case - Save entry with null fields
	@Test
	void testSaveEntryWithNullFields()
	{
		CalendarEntry invalidEntry = new CalendarEntry(); // Missing title/content
		assertThatThrownBy(() -> service.createEntry(invalidEntry))
				.isInstanceOf(IllegalArgumentException.class); // Custom validation needed
	}
}
