package ru.practicum.shareit.item.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemWithRelatedEntities {
    private Item item;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments;

    public ItemWithRelatedEntities(Item item, Booking lastBooking, Booking nextBooking) {
        this.item = item;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }
}
