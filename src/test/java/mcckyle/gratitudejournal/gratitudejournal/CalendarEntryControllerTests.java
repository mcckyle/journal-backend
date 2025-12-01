//***************************************************************************************
//
//     Filename: CalendarEntryControllerTests.java
//     Author: Kyle McColgan
//     Date: 30 November 2025
//     Description: This file contains unit tests for the CalendarEntryController.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal;

import mcckyle.gratitudejournal.gratitudejournal.controller.CalendarEntryController;
import mcckyle.gratitudejournal.gratitudejournal.dto.CalendarEntryDTO;
import mcckyle.gratitudejournal.gratitudejournal.model.CalendarEntry;
import mcckyle.gratitudejournal.gratitudejournal.security.UserDetailsImpl;
import mcckyle.gratitudejournal.gratitudejournal.service.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//***************************************************************************************

@ExtendWith(MockitoExtension.class)
class CalendarEntryControllerTests
{
    @InjectMocks
    private CalendarEntryController controller; // Inject the mocked service into the controller

    @Mock
    private CalendarService service; // Mock the CalendarService

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private CalendarEntry sampleEntry;

    @BeforeEach
    void setUp()
    {
        //Initialize a sample calendar entry.
        sampleEntry = new CalendarEntry();
        sampleEntry.setId(1);
        sampleEntry.setTitle("Sample Title");
        sampleEntry.setContent("Sample Content");
        sampleEntry.setEntryDate(LocalDate.now());
        sampleEntry.setUserId(123);
    }

    private CalendarEntryDTO buildDTOFromEntry(CalendarEntry entry)
    {
        CalendarEntryDTO dto = new CalendarEntryDTO();
        dto.title = entry.getTitle();
        dto.content = entry.getContent();
        dto.entryDate = entry.getEntryDate().toString();
        return dto;
    }

    // Test 1: Controller - Get entries endpoint
    @Test
    void testControllerGetEntries()
    {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        // Arrange: Prepare a sample list of entries.
        List<CalendarEntry> entries = new ArrayList<>();
        entries.add(sampleEntry);

        when(service.getEntriesByUserId(123)).thenReturn(entries); //Required.

        // Act: Call the controller method.
        ResponseEntity<List<CalendarEntry>> response = controller.getEntries(authentication);

        // Assert: Check the response status and body.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo(sampleEntry.getTitle());

        // Verify: Ensure the service was called with the correct userId.
        verify(service).getEntriesByUserId(123);
    }

    // Test 2: Controller - Add new entry endpoint
	@Test
	void testControllerAddEntry()
	{
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        CalendarEntryDTO dto = buildDTOFromEntry(sampleEntry);

        when(service.createEntry(any(CalendarEntry.class))).thenReturn(sampleEntry);

		ResponseEntity<CalendarEntry> response = controller.createEntry(authentication, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getTitle()).isEqualTo("Sample Title");

        verify(service).createEntry(any(CalendarEntry.class));
	}

    //3. Controller - Get entry by ID
    //Test if the controller returns the correct entry by its ID.
    @Test
    void testControllerGetEntryById()
    {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        when(service.getEntryById(123, 1)).thenReturn(Optional.of(sampleEntry)); //Mock the service method.

        // Act: Call the controller method to get entries by user ID
        ResponseEntity<?> response = controller.getEntryById(authentication, 1);

        // Assert: Check that the response status is OK and the list is not empty.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Verify: Ensure the service was called with the correct userId.
        verify(service).getEntryById(123, 1);
    }

    //4. Controller - Get entry by ID Not Found.
    //Test if the controller correctly handles when no entry is found by ID.
    @Test
    void testControllerGetEntryByIdNotFound()
    {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        // Arrange: Mock the service to return an empty list when no entries are found for the user.
        when(service.getEntriesByUserId(123)).thenReturn(Collections.emptyList());

        // Act: Call the controller method to get entries by user ID.
        ResponseEntity<List<CalendarEntry>> response = controller.getEntries(authentication);

        // Assert: Ensure the response status is NO_CONTENT (204) when no entries are found.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify: Ensure the service was called with the correct userId.
        verify(service).getEntriesByUserId(123);
    }

    //5. Controller - Delete entry
    //Test the controller's ability to successfully delete an entry.
    @Test
    void testControllerDeleteEntrySuccess()
    {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        //No exception thrown -> success.
        doNothing().when(service).deleteEntry(123, 1);

        // Act: Call the controller method to delete the entry.
        ResponseEntity<Void> response = controller.deleteEntry(authentication, 1);

        // Assert: Ensure the status is NOT_FOUND (404).
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify: Ensure the service was called with the correct entryId.
        verify(service).deleteEntry(123,1);
    }

    //6. Controller - Delete entry Not Found
    //Test if the controller returns the correct response when trying to delete a non-existent entry.
    @Test
    void testControllerDeleteEntryNotFound()
    {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        // Arrange: Mock the service to throw ResponseStatusException when entry not found.
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendar entry not found"))
                .when(service).deleteEntry(123,1);

        // Act: Call the controller method to delete the entry.
        ResponseEntity<Void> response = controller.deleteEntry(authentication, 1);

        // Assert: Ensure the status is NOT_FOUND (404).
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // Verify: Ensure the service was called with the correct entryId.
        verify(service).deleteEntry(123,1);
    }

    //7. Controller - Update entry
    //Test if the controller correctly handles updating an entry.
    @Test
    void testControllerUpdateEntry()
    {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        CalendarEntryDTO dto = buildDTOFromEntry(sampleEntry);

        // Arrange: Mock the service to return the updated entry.
        when(service.updateEntry(eq(123), eq(123), any(CalendarEntry.class))).thenReturn(sampleEntry);

        // Act: Call the controller method to update the entry.
        ResponseEntity<CalendarEntry> response = controller.updateEntry(authentication,123, dto);

        // Assert: Check that the response status is OK (200) and the body is the updated entry.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(sampleEntry.getTitle());

        // Verify: Ensure the service was called with the correct parameters.
        verify(service).updateEntry(eq(123), eq(123), any(CalendarEntry.class));
    }

    //8. Controller - Update entry Not Found.
    //Test if the controller correctly handles trying to update a non-existent entry.
    @Test
    void testControllerUpdateEntryNotFound()
    {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        CalendarEntryDTO dto = buildDTOFromEntry(sampleEntry);

        // Arrange: Mock the service to return an empty Optional indicating the entry wasn't found.
        when(service.updateEntry(eq(123), eq(123), any(CalendarEntry.class))).thenReturn(null);

        // Act: Call the controller method
        ResponseEntity<CalendarEntry> response = controller.updateEntry(authentication, 123, dto);

        // Assert: Ensure the status code is 404.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // Verify: Ensure the service was called with the correct parameters.
        verify(service).updateEntry(eq(123), eq(123), any(CalendarEntry.class));
    }

    //9. Controller - Handle Bad Request.
    //Test if the controller returns a BAD_REQUEST status when the input is invalid.
    @Test
    void testControllerBadRequest()
    {
        //when(authentication.isAuthenticated()).thenReturn(true);
        //when(authentication.getPrincipal()).thenReturn(userDetails);
        //when(userDetails.getId()).thenReturn(123);

        // Act: Directly call the controller with null input.
        ResponseEntity<CalendarEntry> response = controller.createEntry(authentication, null);

        // Assert: Should return BAD_REQUEST (400).
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        //Verify the service was never called.
        verifyNoInteractions(service);
    }

    //10. Controller - Handle Internal Server Error.
    //Test if the controller returns an INTERNAL_SERVER_ERROR status when an unexpected exception occurs.
    @Test
    void testControllerInternalServerError()
    {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(123);

        CalendarEntryDTO dto = buildDTOFromEntry(sampleEntry);

        // Arrange: Simulate an unexpected exception during service method execution.
        when(service.createEntry(any(CalendarEntry.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Act: Call the controller method.
        ResponseEntity<CalendarEntry> response = controller.createEntry(authentication, dto);

        // Assert: Ensure the status is INTERNAL_SERVER_ERROR (500).
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

//***************************************************************************************