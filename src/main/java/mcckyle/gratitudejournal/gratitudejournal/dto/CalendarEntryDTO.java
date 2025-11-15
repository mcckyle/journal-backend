//***************************************************************************************
//
//     Filename: CalendarEntryDTO.java
//     Author: Kyle McColgan
//     Date: 14 November 2025
//     Description: This file contains the CalendarEntryDTO for LocalDAte conversions.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.dto;

public class CalendarEntryDTO
{
    public String title;
    public String content;
    public String entryDate;
    public Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
