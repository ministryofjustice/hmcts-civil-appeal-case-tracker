import {test} from "@playwright/test";
import LandingPage from "./pages/landingPage";
import SearchPage from "./pages/searchPage";
import details from "./data";

test('Find a case via calendar and go to Summary Case Details', async ({page}) => {
    const landingPage: LandingPage = new LandingPage();
    await landingPage.checkPageLoads(page);
    await landingPage.continueOn(page);

    const searchPage = new SearchPage();
    await searchPage.checkPageLoads(page);
    await searchPage.selectDate(page, details.searchDate.day, details.searchDate.month, details.searchDate.year); // clicks calendar icon, picks the day
    await searchPage.search(page);

    await page.pause();

});
