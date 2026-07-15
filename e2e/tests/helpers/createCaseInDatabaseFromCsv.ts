import { chromium } from '@playwright/test';
import path from 'path';
import AdminLoginPage from '../pages/adminLoginPage';
import AdminUploadPage from '../pages/adminUploadPage';

export default async function createCaseInDatabaseFromCsv(): Promise<void> {
    const adminUser = process.env.ADMIN_USER;
    const adminPass = process.env.ADMIN_PASS;

    if (!adminUser || !adminPass) {
        throw new Error('ADMIN_USER and ADMIN_PASS env vars must be set to upload the CSV fixture');
    }

    const browser = await chromium.launch();
    const page = await browser.newPage();

    try {
        const loginPage = new AdminLoginPage();
        await loginPage.checkPageLoads(page);
        await loginPage.loginAndContinueOn(page, adminUser, adminPass);

        const uploadPage = new AdminUploadPage();
        await uploadPage.checkPageLoads(page);
        await uploadPage.uploadFile(page, path.join(__dirname, '..', 'data', 'CASE_TRACKER.csv'));
        await uploadPage.importIntoDatabase(page);
        await uploadPage.checkImportMessage(page, 'rows added in database');
    } finally {
        await browser.close();
    }
}
