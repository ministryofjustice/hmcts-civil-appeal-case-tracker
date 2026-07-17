import { Page } from 'playwright';
import { expect } from '@playwright/test';
import caseDetailsPage_content from '../content/caseDetailsPage_content';

interface CaseDetails {
    reference: string;
    title: string;
    type: string;
    appeal: string;
    hearingStatus: string;
    venue: string;
    constitution: string;
    caseResult: string;
    currentStatus: string;
    lastUpdated: string;
    trackingLine1: string;
}

class CaseDetailsPage {
    private readonly title: string;
    private readonly text: string;

    constructor() {
        this.title = `heading`;
        this.text = `#Content .holder`;
    }

    async checkPageLoads(page: Page): Promise<void> {
        await Promise.all([
            expect(page.getByRole(this.title as 'heading', { level: 1 })).toContainText(caseDetailsPage_content.pageTitle),
            expect(page.getByRole(this.title as 'heading', { level: 2 })).toContainText(caseDetailsPage_content.sectionHeadingPrefix),
            expect(page.locator(this.text)).toContainText(caseDetailsPage_content.caseResultsHeading),
            expect(page.locator(this.text)).toContainText(caseDetailsPage_content.trackYourCaseHeading),
            expect(page.locator(this.text)).toContainText(caseDetailsPage_content.trackingInformationHeading),
        ]);
    }

    // venue and caseResult are blank for some cases (see data.ts) so those
    // checks are skipped rather than asserting on an always-true empty string.
    async checkCaseDetails(page: Page, details: CaseDetails): Promise<void> {
        const checks = [
            expect(page.locator(this.text)).toContainText(details.reference),
            expect(page.locator(this.text)).toContainText(details.title),
            expect(page.locator(this.text)).toContainText(details.type),
            expect(page.locator(this.text)).toContainText(details.appeal),
            expect(page.locator(this.text)).toContainText(details.hearingStatus),
            expect(page.locator(this.text)).toContainText(details.constitution),
            expect(page.locator(this.text)).toContainText(details.currentStatus),
            expect(page.locator(this.text)).toContainText(details.lastUpdated),
            expect(page.locator(this.text)).toContainText(details.trackingLine1),
        ];

        if (details.venue) {
            checks.push(expect(page.locator(this.text)).toContainText(details.venue));
        }
        if (details.caseResult) {
            checks.push(expect(page.locator(this.text)).toContainText(details.caseResult));
        }

        await Promise.all(checks);
    }
}

export default CaseDetailsPage;
