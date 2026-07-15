import { Page } from 'playwright';
import { expect } from '@playwright/test';
import landingPage_content from '../content/landingPage_content';
// import axeTest from '../accessibilityTestHelper';

class LandingPage {
    private readonly url: string;
    private readonly title: string;
    private readonly text: string;

    constructor() {
        this.url = '/';
        this.title = `heading`;
        this.text = `#Content .holder`;
    }

    async checkPageLoads(page: Page): Promise<void> {
        // Navigate to the landing page
        await page.goto(this.url);

        // Check elements of the page
        await Promise.all([
            expect(page.getByRole(this.title as 'heading', { level: 1 })).toContainText(landingPage_content.pageTitle),
            expect(page.locator(this.text)).toContainText(landingPage_content.pText1),
            expect(page.locator(this.text)).toContainText(landingPage_content.pText2),
            expect(page.locator(this.text)).toContainText(landingPage_content.pText3),
            expect(page.locator(this.text)).toContainText(landingPage_content.liText1),
            expect(page.locator(this.text)).toContainText(landingPage_content.liText2),
            expect(page.locator(this.text)).toContainText(landingPage_content.liText3),

        ]);
        // await axeTest(this.page);
    }

    async continueOn(page: Page): Promise<void> {
        await page.getByRole('link', { name: 'Next' }).click();
    }
}

export default LandingPage;