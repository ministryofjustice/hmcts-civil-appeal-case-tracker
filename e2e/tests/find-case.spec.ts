import {test} from "@playwright/test";
import LandingPage from "./pages/landingPage";
import SearchPage from "./pages/searchPage";
import details from "./data";
import createCaseInDatabaseFromCsv from "./helpers/createCaseInDatabaseFromCsv";
import CaseDetailsPage from "./pages/caseDetailsPage";

test.beforeAll('Admin uploads csv to database', async () => {
    await createCaseInDatabaseFromCsv();
});

test('Find a case via calendar and go to Summary Case Details', async ({page}) => {
    const landingPage: LandingPage = new LandingPage();
    await landingPage.checkPageLoads(page);
    await landingPage.continueOn(page);

    const searchPage = new SearchPage();
    await searchPage.checkPageLoads(page);
    await searchPage.selectDate(page, details.searchDate.day, details.searchDate.month, details.searchDate.year); // clicks calendar icon, picks the day
    await searchPage.search(page);
    await searchPage.selectRecordAndContinueOn(page, details.searchCaseReference.caseRef2);

    const casePage: CaseDetailsPage = new CaseDetailsPage();
    await casePage.checkPageLoads(page);
    await casePage.checkCaseDetails(page, details.caseDetails.caseDetails2);
});
