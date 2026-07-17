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
            expect(page.locator('input[name="uploadfile"]')).toBeVisible(),
            expect(page.locator('input[name="Submit"]')).toBeVisible(),
            expect(page.locator('input[name="Reset"]')).toBeVisible(),
            expect(page.getByRole('link', { name: 'Click here to add data in database'})).toBeVisible()
        ]);
    }

    async uploadFile(page: Page, filePath: string): Promise<void> {
        await page.locator('input[name="uploadfile"]').setInputFiles(filePath);
        await page.getByRole('button', { name: 'Upload Form' }).click();
    }

    async importIntoDatabase(page: Page): Promise<void> {
        await page.getByRole('link', { name: 'Click here to add data in database' }).click();
    }

    async checkImportMessage(page: Page, expectedMessage: string): Promise<void> {
        await expect(page.locator(this.text)).toContainText(expectedMessage);
    }
}

export default AdminUploadPage;
