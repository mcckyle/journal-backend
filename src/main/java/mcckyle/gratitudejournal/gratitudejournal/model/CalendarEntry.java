//***************************************************************************************
//
//     Filename: CalendarEntry.java
//     Author: Kyle McColgan
//     Date: 04 December 2024
//     Description: This file contains the CalendarEntry object structure.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

//***************************************************************************************

@Entity
@Table(name = "calendar_entries")
public class CalendarEntry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;

    @NotNull(message = "User ID is required")
    @Column(nullable = false) //Ensure userId is required.
    private Integer userId;  // Store user ID as a reference (not a foreign key)

    // Default constructor
    public CalendarEntry() {}

    // Constructor with all fields
    public CalendarEntry(String title, String content, LocalDate entryDate, Integer userId)
    {
        this.title = title;
        this.content = content;
        this.entryDate = entryDate;
        this.userId = userId;
    }

    // Getters and Setters
    public Integer getId() { return id; }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public LocalDate getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate)
    {
        this.entryDate = entryDate;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "CalendarEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", entryDate=" + entryDate +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEntry that = (CalendarEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(entryDate, that.entryDate) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title, content, entryDate, userId);
    }
}
