package com.example.dnatask.joboffer;

import com.example.dnatask.joboffer.job.model.Category;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public record CreateJobOfferRequest(@NotNull Category category,
                             @NotNull Instant startDate,
                             @NotNull Instant endDate,
                             @NotNull Long employerId) {
}
