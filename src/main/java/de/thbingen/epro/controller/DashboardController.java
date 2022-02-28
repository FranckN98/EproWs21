package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.dashboard.DashboardItem;
import de.thbingen.epro.service.DashboardService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Gets the current dashboard containing all {@link de.thbingen.epro.model.entity.BusinessUnitObjective}s that are
     * currently active and its {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}s as well as the currently
     * active {@link de.thbingen.epro.model.entity.CompanyObjective}s with their {@link de.thbingen.epro.model.entity.CompanyKeyResult}s
     *
     * @return the current dashboard
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public CollectionModel<DashboardItem> getDashboard() {
        CollectionModel<DashboardItem> collectionModel = CollectionModel.of(dashboardService.getDashboardItems());
        collectionModel.add(linkTo(methodOn(DashboardController.class).getDashboard()).withSelfRel());
        return collectionModel;
    }
}
