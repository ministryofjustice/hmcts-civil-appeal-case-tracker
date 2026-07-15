import { Page } from 'playwright';
import { expect } from '@playwright/test';
import searchPage_content from '../content/searchPage_content';
// import axeTest from '../accessibilityTestHelper';

class SearchPage {
    private readonly url: string;
    private readonly title: string;
    private readonly text: string;

    constructor() {
        this.url = 'https://casetracker.justice.gov.uk/search.jsp';
        this.title = `heading`;
        this.text = `#Content .holder`;
    }

    async checkPageLoads(page: Page): Promise<void> {
        await page.goto(this.url);

        // Check elements of the page
        await Promise.all([
            expect(page.getByRole(this.title as 'heading', { level: 1 })).toContainText(searchPage_content.pageTitle),
            expect(page.getByRole(this.title as 'heading', { level: 2 })).toContainText(searchPage_content.sectionHeading),
            expect(page.locator(this.text)).toContainText(searchPage_content.pText1),

        ]);
        // await axeTest(this.page);
    }

    async searchFor(page: Page, searchTerm: string): Promise<void> {
        await page.locator('#search').fill(searchTerm);
        await page.getByRole('button', { name: 'Search' }).click();
    }

    async goBack(page: Page): Promise<void> {
        await page.getByRole('link', { name: 'Back' }).click();
    }

    // Opens the HTMLCalendar.htm popup, We close the popup rather than waiting for its
    // own window.close(): that call gets silently blocked by Chromium
    async selectDate(page: Page, day: string, month?: string, year?: string): Promise<void> {
        const [popup] = await Promise.all([
            page.waitForEvent('popup'),
            page.locator('a[href*="ShowCalendar"]').click(),
        ]);
        await popup.waitForLoadState();

        if (month) {
            await popup.locator('#cboMonth').selectOption({ label: month });
        }
        if (year) {
            await popup.locator('#cboYear').selectOption({ label: year });
        }

        await popup
            .locator('#ButtonPanel a')
            .getByText(String(day), { exact: true })
            .click();

        await expect(page.locator('#search')).not.toHaveValue('');

        if (!popup.isClosed()) {
            await popup.close();
        }
    }

    async search(page: Page): Promise<void> {
        await page.getByRole('button', { name: 'Search' }).click();
    }
}

export default SearchPage;
