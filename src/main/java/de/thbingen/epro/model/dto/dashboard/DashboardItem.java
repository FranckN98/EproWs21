package de.thbingen.epro.model.dto.dashboard;

import org.springframework.hateoas.server.core.Relation;

import java.util.List;

/**
 * Container for a single company dashboard item, which holds multiple {@link de.thbingen.epro.model.entity.CompanyObjective}s and the respective {@link de.thbingen.epro.model.entity.CompanyKeyResult}s
 * as well as a list of {@link BusinessUnitDashboardItem} which hold {@link BusinessUnitDashboardItem} containing
 * {@link de.thbingen.epro.model.entity.BusinessUnitObjective} and the respective {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}s
 */
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
