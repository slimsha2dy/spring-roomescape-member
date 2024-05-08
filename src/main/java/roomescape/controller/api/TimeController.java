package roomescape.controller.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class TimeController {

    ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTimes();

        return ResponseEntity.ok()
                .body(responses);
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> addTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse response = reservationTimeService.addReservationTime(
                request);
        URI location = URI.create("/times/" + response.id());

        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") Long id) {
        reservationTimeService.deleteReservationTimeById(id);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/times/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getReservationTimeBookedStatus(
            @RequestParam LocalDate date,
            @RequestParam Long themeId) {
        List<AvailableReservationTimeResponse> responses = reservationTimeService.getReservationTimeBookedStatus(
                date, themeId);

        return ResponseEntity.ok()
                .body(responses);
    }
}
