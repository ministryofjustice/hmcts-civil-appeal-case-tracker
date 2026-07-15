const data = {
    searchDate: {
        day: "15",
        month: "January",
        year: "2024"
    },
    searchCaseTitle: {
        caseTitle1: 'ALPHA CONSULTING LIMITED v BETA SERVICES LTD',
        caseTitle2: 'SMITH v JONES',
        caseTitle3: 'OMEGA TECHNOLOGIES LTD v MINISTRY OF INFRASTRUCTURE',
    },
    searchCaseReference: {
        caseRef1: 'CA-2024-000101',
        caseRef2: 'CA-2024-000102',
        caseRef3: 'CA-2024-000103',
    },
    caseDetails: {
        caseDetails1: {
            reference: 'CA-2024-000101',
            title: 'ALPHA CONSULTING LIMITED v BETA SERVICES LTD',
            type: 'Appeal',
            appeal: 'from the order of HHJ Williams',
            hearingStatus: 'Hear By 15-Jan-2026',
            venue: '',
            constitution: 'Not assigned',
            caseResult: '',
            currentStatus: "Awaiting bundles and/or documents from applicant/applicant's solicitors",
            lastUpdated: '06-Nov-2025',
            trackingLine1: '10-Jul-2025: Case file passed to Case Management'
        },
        caseDetails2: {
            reference: 'CA-2024-000102',
            title: 'SMITH v JONES',
            type: 'Appeal',
            appeal: 'from the order of HHJ Carter',
            hearingStatus: 'Fixed on 12-Apr-2021 - estimated length (in hours): 0:30',
            venue: '',
            constitution: 'Not assigned',
            caseResult: 'Allowed on 12-Apr-2021',
            currentStatus: 'Awaiting a hearing - see Hearing Status',
            lastUpdated: '06-Nov-2025',
            trackingLine1: '20-Dec-2024: Case passed to List Office'
        },
        caseDetails3: {
            reference: 'CA-2024-000103',
            title: 'OMEGA TECHNOLOGIES LTD v MINISTRY OF INFRASTRUCTURE',
            type: 'Appeal',
            appeal: 'from the order of Recorder James Harper KC',
            hearingStatus: 'Fixed on 08-Feb-2023 - estimated length (in hours): 6:00',
            venue: 'London',
            constitution: 'Not assigned',
            caseResult: 'Judgment reserved (CAV) on 08-Feb-2023',
            currentStatus: 'Awaiting a Judicial decision on the papers',
            lastUpdated: '06-Nov-2025',
            trackingLine1: '12-Aug-2022: Application referred to Lord/Lady Justice'
        },
        caseDetails4: {
            reference: 'CA-2024-000104',
            title: 'GREEN ESTATES LTD v HARRIS & CO',
            type: 'Appeal',
            appeal: 'from the order of HHJ Peterson',
            hearingStatus: 'Fixed on 18-Aug-2021 - estimated length (in hours): 0:20',
            venue: '',
            constitution: 'Not assigned',
            caseResult: 'Refused on 18-Aug-2021',
            currentStatus: 'Awaiting a hearing - see Hearing Status',
            lastUpdated: '06-Nov-2025',
            trackingLine1: '15-Mar-2021: Case passed to List Office'
        }
    }
} as const;

export default data;
