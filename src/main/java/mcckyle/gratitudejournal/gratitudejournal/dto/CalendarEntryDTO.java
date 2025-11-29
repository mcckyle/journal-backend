//***************************************************************************************
//
//     Filename: CalendarEntryDTO.java
//     Author: Kyle McColgan
//     Date: 26 November 2025
//     Description: This file contains the CalendarEntryDTO for Date object conversions.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CalendarEntryDTO
{
    @NotBlank
    @Size(max = 100)
    public String title;

    @NotBlank
    @Size(max = 5000)
    public String content;

    @NotBlank
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Date must be in YYYY-MM-DD format."
    )
    public String entryDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
