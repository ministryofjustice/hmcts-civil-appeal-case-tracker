# Testing Strategy

This document describes what automated testing exists for the Civil Appeals Case Tracker,
how to run it, and what's still manual.

## At a glance

| Layer | Tool | Location | Tag | Runs in CI |
|---|---|---|---|---|
| Smoke | Bash/curl | `bin/smoke-test.sh` | - | Yes (before e2e suites, DEV build) |
| End-to-end (UI) | Playwright | `e2e/tests/*.spec.ts` | `@e2e` | Yes |
| Accessibility | Playwright + axe-core | `e2e/tests/accessibility.spec.ts` | `@accessibility` | Yes |


## Running the suites locally

The app must be running and reachable (defaults to `http://localhost:8080`, override
with `BASE_URL`). All commands below are run from the `e2e/` directory.

```bash
cd e2e
corepack enable
yarn install
yarn playwright install --with-deps chromium
```

Admin-flow tests (CSV upload, and any suite that seeds data through the admin upload
page) need real admin credentials. Copy `e2e/.env.example` to `e2e/.env` and set:

```
ADMIN_USER=<admin username>
ADMIN_PASS=<admin password>
```

Then:

```bash
yarn test:e2e            # user-facing journeys (@e2e)
yarn test:accessibility  # axe-core scans (@accessibility)
yarn test                # everything, headed, chromium only
yarn test:all            # everything, headed, chromium only, no tag filter
yarn report              # open the last HTML report
```

To run the smoke tests on their own against a running instance:

```bash
BASE_URL=http://localhost:8080 bin/smoke-test.sh
```

## What's covered

### Smoke (`bin/smoke-test.sh`)

Fast, browser-free curl checks meant to catch a badly broken deploy (wrong DB creds, WAR
not deployed, static assets missing) in seconds, before installing Yarn/Chromium for the
Playwright suites. Checks: `/health`, the landing page renders, `/search.jsp` renders,
`/loginform.do` renders, a static asset is served with the right content type, a real
search reaches the database, and an unknown case reference is handled gracefully.

### End-to-end (`@e2e`)

- **`find-case.spec.ts`** - the three ways a user can find a case (`Ways to Search`):
  by calendar date, by case title, and by case reference. Each seeds the database from
  the CSV fixture (`e2e/tests/data/CASE_TRACKER.csv`) via the admin upload flow, then
  drives the full journey through to the Case Details page and asserts the correct
  record is shown.
- **`admin-upload.spec.ts`** - admin login followed by CSV upload and database import,
  asserting the confirmation message.

### Accessibility (`@accessibility`)

`accessibility.spec.ts` runs an axe-core scan (WCAG 2.0/2.1/2.2, levels A and AA) against
every distinct page state in the app: landing page, search page (empty and with results),
case details page, admin login page, and admin upload page. A test fails if axe reports
any violation.

## Page Object Model

Tests use a page-object pattern under `e2e/tests/pages/` (one class per page, exposing
`checkPageLoads` plus the actions available on that page) with expected copy pulled from
`e2e/tests/content/*_content.ts` and test fixtures from `e2e/tests/data.ts`. Follow this
pattern for new tests rather than driving locators directly from spec files - it's what
keeps the accessibility suite and the e2e suite able to reuse the same page objects.

## Adding a new test

1. Reuse an existing page object where possible, or add a method to it if the journey
   needs a new interaction.
2. Add expected copy to the relevant `*_content.ts` file rather than inlining strings.
3. Tag the `test.describe` block with `@e2e` or `@accessibility` (or a new tag, updating
   the `package.json` scripts and this doc if you introduce one) so it's picked up by
   the right CI step and `yarn` script.
