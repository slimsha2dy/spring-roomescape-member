package roomescape.controller.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        List<ThemeResponse> responses = themeService.getAllThemes();

        return ResponseEntity.ok()
                .body(responses);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody ThemeRequest request) {
        ThemeResponse response = themeService.addTheme(request);
        URI location = URI.create("/theme/" + response.id());

        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/themes/rankings")
    public ResponseEntity<List<ThemeResponse>> getTopThemes() {
        List<ThemeResponse> responses = themeService.getTopThemes();

        return ResponseEntity.ok()
                .body(responses);
    }
}
