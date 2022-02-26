package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.dashboard.DashboardItem;
import de.thbingen.epro.service.DashboardService;
import org.springframework.hateoas.CollectionModel;
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

    @GetMapping
    public CollectionModel<DashboardItem> getDashboard() {
        CollectionModel<DashboardItem> collectionModel = CollectionModel.of(dashboardService.getDashboardItems());
        collectionModel.add(linkTo(methodOn(DashboardController.class).getDashboard()).withSelfRel());
        return collectionModel;
    }
}
