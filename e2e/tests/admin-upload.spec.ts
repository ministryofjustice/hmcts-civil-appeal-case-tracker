import { test } from '@playwright/test';
import path from 'path';
import AdminLoginPage from './pages/adminLoginPage';
import AdminUploadPage from './pages/adminUploadPage';

test.describe('Admin Upload',  { tag: '@e2e' }, () => {
    test('Admin can upload a CSV and import it into the database', async ({ page }) => {
        const adminUser = process.env.ADMIN_USER;
        const adminPass = process.env.ADMIN_PASS;

        if (!adminUser || !adminPass) {
            throw new Error('ADMIN_USER and ADMIN_PASS env vars must be set to run this test');
        }

        const loginPage = new AdminLoginPage();
        await loginPage.checkPageLoads(page);
        await loginPage.loginAndContinueOn(page, adminUser, adminPass);

        const uploadPage = new AdminUploadPage();
        await uploadPage.checkPageLoads(page);
        await uploadPage.uploadFile(page, path.join(__dirname, 'data', 'CASE_TRACKER.csv'));
        await uploadPage.importIntoDatabase(page);
        await uploadPage.checkImportMessage(page, '10 rows added in database');
    });
});
