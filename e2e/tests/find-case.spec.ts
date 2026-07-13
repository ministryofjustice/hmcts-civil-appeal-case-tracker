import {test} from "@playwright/test";
import LandingPage from "./pages/landingPage";

test('Find a case and go to Summary Case Details', async ({page}) => {
    const landingPage: LandingPage = new LandingPage();
    await landingPage.checkPageLoads(page);
    await landingPage.continueOn(page);

});
