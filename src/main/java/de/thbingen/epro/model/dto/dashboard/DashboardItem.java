package de.thbingen.epro.model.dto.dashboard;

import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(collectionRelation = "dashboardItems", itemRelation = "dashboardItem")
public class DashboardItem {

    private CompanyDashboardItem companyDashboardItem;
    private List<BusinessUnitDashboardItem> businessUnitDashboardItems;

    public DashboardItem() {
    }

    public DashboardItem(List<BusinessUnitDashboardItem> businessUnitDashboardItems, CompanyDashboardItem companyDashboardItem) {
        this.businessUnitDashboardItems = businessUnitDashboardItems;
        this.companyDashboardItem = companyDashboardItem;
    }

    public List<BusinessUnitDashboardItem> getBusinessUnitDashboardItems() {
        return businessUnitDashboardItems;
    }

    public void setBusinessUnitDashboardItems(List<BusinessUnitDashboardItem> businessUnitDashboardItems) {
        this.businessUnitDashboardItems = businessUnitDashboardItems;
    }

    public CompanyDashboardItem getCompanyDashboardItem() {
        return companyDashboardItem;
    }

    public void setCompanyDashboardItem(CompanyDashboardItem companyDashboardItem) {
        this.companyDashboardItem = companyDashboardItem;
    }
}
