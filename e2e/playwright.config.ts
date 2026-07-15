import { defineConfig, devices } from '@playwright/test';
import dotenv from 'dotenv';
import path from 'path';

// Loads e2e/.env into process.env before anything below
dotenv.config({ path: path.resolve(__dirname, '.env') });

module.exports = defineConfig({
  testDir: "./tests",
  /* Run tests in files in parallel */
  fullyParallel: true,
  /* Fail the build on CI if you accidentally left test.only in the source
  code. */
  forbidOnly: !!process.env.CI,
  /* Retry on CI only */
  retries: 4, // Set the number of retries for all projects
  timeout: 5 * 60 * 1000, // How long before a test fails due to running for too long
  expect: {
    timeout: 60 * 1000,
  }, // How long an individual expect() function will fail
  reportSlowTests: null,
  workers: process.env.FUNCTIONAL_TESTS_WORKERS ? 5 : 5,
// The number of tests that can run in parallel
  reporter: process.env.CI ? "html" : "html",
// How the tests will be reported, see playwright.dev reporters for more.
  projects: [
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
        channel: "chrome", // Desktop Chrome
        trace: "retain-on-first-failure", // Gives a playwright trace on failure
        javaScriptEnabled: true, // Enables Javascript in the browser
      },
    },
    {
      name: "firefox",
      use: {
        ...devices["Desktop Firefox"], // Desktop firefox
        screenshot: "off", // Decides whether it screenshots on failure
        trace: "retain-on-first-failure",
        javaScriptEnabled: true,
      },
    },
    {
      name: "webkit",
      use: {
        ...devices["Desktop Safari"], // Desktop Safari
        screenshot: "off",
        trace: "retain-on-first-failure",
        javaScriptEnabled: true,
      },
    },
    {
      name: "MobileChrome",
      use: {
        ...devices["Pixel 5"], // Google pixel 5 Chrome
        screenshot: "only-on-failure",
        trace: "off",
      },
    },
    {
      name: "MobileSafari",
      use: {
        ...devices["iPhone 12"], // iPhone 12 Safari
        screenshot: "only-on-failure",
        trace: "off",
      },
    },
    {
      name: "MicrosoftEdge",
      use: {
        ...devices["Desktop Edge"], // MS Edge desktop
        channel: "msedge",
        screenshot: "only-on-failure",
        trace: "off",
      },
    },
  ],
});