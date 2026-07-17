import { test } from '@playwright/test';

import createCaseInDatabaseFromCsv from './helpers/createCaseInDatabaseFromCsv';
import axeTest from './helpers/accessibilityTestHelper';
import LandingPage from './pages/landingPage';
import SearchPage from './pages/searchPage';
import CaseDetailsPage from './pages/caseDetailsPage';
import AdminLoginPage from './pages/adminLoginPage';
import AdminUploadPage from './pages/adminUploadPage';
import details from './data';

test.describe('Accessibility Tests', { tag: '@accessibility' }, () => {
    test.beforeAll('Admin uploads csv to database', async () => {
        await createCaseInDatabaseFromCsv();
    });

    test('Landing page has no accessibility violations', async ({ page }) => {
        const landingPage = new LandingPage();
        await landingPage.checkPageLoads(page);

        await axeTest(page);
    });

    test('Search page has no accessibility violations', async ({ page }) => {
        const landingPage = new LandingPage();
        await landingPage.checkPageLoads(page);
        await landingPage.continueOn(page);

        const searchPage = new SearchPage();
        await searchPage.checkPageLoads(page);

        await axeTest(page);
    });

    test('Search results have no accessibility violations', async ({ page }) => {
        const landingPage = new LandingPage();
        await landingPage.checkPageLoads(page);
        await landingPage.continueOn(page);

        const searchPage = new SearchPage();
        await searchPage.checkPageLoads(page);
        await searchPage.selectDate(page, details.searchDate.day, details.searchDate.month, details.searchDate.year);
        await searchPage.search(page);

        await axeTest(page);
    });

    test('Case details page has no accessibility violations', async ({ page }) => {
        const landingPage = new LandingPage();
        await landingPage.checkPageLoads(page);
        await landingPage.continueOn(page);

        const searchPage = new SearchPage();
        await searchPage.checkPageLoads(page);
        await searchPage.selectDate(page, details.searchDate.day, details.searchDate.month, details.searchDate.year);
        await searchPage.search(page);
        await searchPage.selectRecordAndContinueOn(page, details.searchCaseReference.caseRef2);

        const casePage = new CaseDetailsPage();
        await casePage.checkPageLoads(page);

        await axeTest(page);
    });

    test('Admin login page has no accessibility violations', async ({ page }) => {
        const loginPage = new AdminLoginPage();
        await loginPage.checkPageLoads(page);

        await axeTest(page);
    });

    test('Admin upload page has no accessibility violations', async ({ page }) => {
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

        await axeTest(page);
    });
});
