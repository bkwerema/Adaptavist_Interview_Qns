import com.atlassian.jira.component.ComponentAccessor

def groupManager = ComponentAccessor.getGroupManager()

groupManager.isUserInGroup(currentUser, 'jira-administrators') && issue.status?.name == "IN REVIEW"










