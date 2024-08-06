package ru.pracicum.shareit.booking.util.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.pracicum.shareit.booking.model.dto.CreateBookingDto;


import java.time.LocalDateTime;

public class CheckStartAndEndDateValidator implements ConstraintValidator<BookingStartDateBeforeEndDate, CreateBookingDto> {
    @Override
    public void initialize(BookingStartDateBeforeEndDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateBookingDto createBookingDto, ConstraintValidatorContext context) {
        LocalDateTime start = createBookingDto.start();
        LocalDateTime end = createBookingDto.end();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}
