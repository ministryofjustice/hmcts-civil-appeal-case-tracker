#!/usr/bin/env bash
# Fast, browser-free sanity checks against a running app instance - run this
# before the Playwright e2e suite so a badly broken deploy (wrong DB creds,
# WAR not deployed, static assets missing) fails in seconds instead of after
# installing Yarn deps and Chromium.
set -uo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
FAILED=0

check() {
    local name="$1"
    local path="$2"
    local expected_status="$3"
    local expected_body="${4:-}"
    local expected_content_type="${5:-}"
    local body_file header_file
    body_file="$(mktemp)"
    header_file="$(mktemp)"

    local status
    status="$(curl -sS -D "$header_file" -o "$body_file" -w "%{http_code}" "${BASE_URL}${path}")"

    if [ "$status" != "$expected_status" ]; then
        echo "FAIL: $name - expected status $expected_status, got $status (${BASE_URL}${path})"
        FAILED=1
    elif [ -n "$expected_body" ] && ! grep -q "$expected_body" "$body_file"; then
        echo "FAIL: $name - response did not contain \"$expected_body\" (${BASE_URL}${path})"
        FAILED=1
    elif [ -n "$expected_content_type" ] && ! grep -qi "^content-type:.*${expected_content_type}" "$header_file"; then
        echo "FAIL: $name - expected Content-Type \"$expected_content_type\", got: $(grep -i '^content-type:' "$header_file" | tr -d '\r')"
        FAILED=1
    else
        echo "PASS: $name"
    fi

    rm -f "$body_file" "$header_file"
}

echo "Running smoke tests against ${BASE_URL}"
echo

check "Health check"                     "/health"                              200 "OK"
check "Landing page renders"             "/"                                    200 "Case Tracker for Civil Appeals"
check "Ways to Search page renders"      "/search.jsp"                          200 "Ways to Search"
check "Admin login page renders"         "/loginform.do"                        200 "Login Form"
check "Static assets are served"         "/asset/css/dg.css"                    200 "" "text/css"
check "Search reaches the database"      "/search.do?search=a"                  200
check "Unknown case handled gracefully"  "/getDetail.do?case_id=DOES-NOT-EXIST" 200

echo
if [ "$FAILED" -ne 0 ]; then
    echo "Smoke tests FAILED"
    exit 1
fi

echo "All smoke tests passed"
