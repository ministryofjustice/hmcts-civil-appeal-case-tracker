import { Page } from 'playwright';
import { expect } from '@playwright/test';
import adminLoginPage_content from '../content/adminLoginPage_content';

class AdminLoginPage {
    private readonly url: string;
    private readonly title: string;
    private readonly text: string;

    constructor() {
        this.url = 'http://localhost:8080/loginform.do';
        this.title = `heading`;
        this.text = `#Content .holder`;
    }

    async checkPageLoads(page: Page): Promise<void> {
        await page.goto(this.url);

        await Promise.all([
            expect(page.getByRole(this.title as 'heading', { level: 1 })).toContainText(adminLoginPage_content.pageTitle),
            expect(page.getByRole(this.title as 'heading', { level: 2 })).toContainText(adminLoginPage_content.sectionHeading),
        ]);
    }

    // Credentials come from ADMIN_USER/ADMIN_PASS env vars (validateLogin.jsp
    // checks against the same vars server-side) - never hardcode them here.
    async loginAndContinueOn(page: Page, userId: string, password: string): Promise<void> {
        await page.locator('input[name="userid"]').fill(userId);
        await page.locator('input[name="password"]').fill(password);
        await page.getByRole('button', { name: 'Submit' }).click();
    }
}

export default AdminLoginPage;
