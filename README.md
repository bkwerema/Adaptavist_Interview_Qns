Question 1 - qn1_condition and q1_additional_actions

This script automatically detects if the parent issue has transitioned into "In Review"

- It also checks whether the acting user is part of the `jira-administrators` group.
- If both conditions are met:
  - It fetches all subtasks of the issue and transitions all of them to "Done' regardless of what their current status is.

Key Features

- Uses 'ScriptRunner Groovy' with Jira's Workflow API
- Reloads each subtask after each transition to ensure the current status is up-to-date
- Logs all transitions and errors for debugging

How to Use

- Place the script inside a **ScriptRunner listener**, triggered on the "Issue Updated" event

Question 2
IMPROVEMENTS MADE TO THE ORIGINAL SCRIPT
Issues with the original implementation:

- No input validation was present to ensure the issueKey parameter was provided.
- If the issueKey was missing or incorrect, the response returned a vague error message like "the issue does not exist".
- There were no checks to confirm whether the issue was successfully retrieved.
- No validation existed to handle cases where the issue had no parent.
- The script did not verify if the parent key was retrieved before attempting to return a response.

Enhancements Introduced:

- Added comprehensive error handling and input validation to provide more informative error messages.
  Implemented checks for:
- Missing or empty issueKey parameter
- Invalid or non-existent issues
- Issues that do not have a parent

How to Use the Endpoint:
Navigate to ScriptRunner > REST Endpoints, and create a new custom endpoint.
Paste the updated code into the editor.
Call the endpoint using a URL like:
http://localhost:8090/rest/scriptrunner/latest/custom/getParentIssueKey?issueKey=SP-24

NOTE: Ensure your test account belongs to the appropriate group.
In my case: The access group was changed from "adaptavist-users" to "jira-software-users" to match the groups that include my test account.
