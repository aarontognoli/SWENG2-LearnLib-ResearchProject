package Domus.DatasetUtils;

import java.time.LocalDateTime;

public record DomusActivityRecord(LocalDateTime start,LocalDateTime end,String activityId) {
}
