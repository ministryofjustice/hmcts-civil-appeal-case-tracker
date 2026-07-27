import { Page } from 'playwright';
import { expect } from '@playwright/test';
import adminUploadPage_content from '../content/adminUploadPage_content';
import landingPage_content from "../content/landingPage_content";

class AdminUploadPage {
    private readonly title: string;
    private readonly text: string;

    constructor() {
        this.title = `heading`;
        this.text = `#Content .holder`;
    }

    async checkPageLoads(page: Page): Promise<void> {

        await Promise.all([
            expect(page.getByRole(this.title as 'heading', { level: 1 })).toContainText(adminUploadPage_content.pageTitle),
            expect(page.getByRole(this.title as 'heading', { level: 2 })).toContainText(adminUploadPage_content.sectionHeading),
            expect(page.locator(this.text)).toContainText(adminUploadPage_content.selectFileText),
            expect(page.locator('input[name="uploadFile"]')).toBeVisible(),
            expect(page.locator('input[name="submit"]')).toBeVisible(),
            expect(page.locator('input[name="reset"]')).toBeVisible()
        ]);
    }

    async uploadFile(page: Page, filePath: string): Promise<void> {
        await page.locator('input[name="uploadFile"]').setInputFiles(filePath);

    }

    async importIntoDatabase(page: Page): Promise<void> {
        await page.getByRole('button', { name: 'Upload and Import' }).click();
    }

    async checkImportMessage(page: Page, expectedMessage: string): Promise<void> {
        await expect(page.locator(this.text)).toContainText(expectedMessage);
    }
}

export default AdminUploadPage;
