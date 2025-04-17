import com.atlassian.jira.component.ComponentAccessor
import com.opensymphony.workflow.loader.ActionDescriptor

def workflowManager = ComponentAccessor.getWorkflowManager()
def issueManager = ComponentAccessor.getIssueManager()

// Retrieve Sub Tasks using the parents issue key - "SP-10 WAS used for testing"
def allSubtasks = Issues.getByKey(issue.key).getSubTaskObjects()

// Define your transition path (make sure status names match Jira config exactly)
def transitionSequence = ["To Do", "In Progress", "In Review", "Done"]

//logs for troubleshooting
def logs = []

//Iterate through the sub tasks to 
allSubtasks.each { subTask ->
    def currentStatus = subTask.status.name
    def subTaskKey = subTask.key

    if( currentStatus == 'TO DO'){currentStatus = 'To Do'}
    else if( currentStatus == 'IN PROGRESS'){currentStatus = 'In Progress'}
    else if( currentStatus == 'IN REVIEW '){currentStatus = 'In Review'}
    else if( currentStatus == 'DONE'){currentStatus = 'Done'}

    def currentIndex = transitionSequence.indexOf(currentStatus)

    if (currentIndex == -1) {
        logs << "${subTaskKey}: Unknown status '${currentStatus}'"
        return
    }
    
    // Transition through each step until we reach 'Done'
    for (int i = currentIndex; i < transitionSequence.size() - 1; i++) {
        def fromStatus = transitionSequence[i]
        def toStatus = transitionSequence[i + 1]


        // Always refetch the status info the latest state of the sub-task
        def refreshedSubTask = issueManager.getIssueObject(subTaskKey)
        def refreshedStatus = refreshedSubTask.getStatus().getName()
        def workflow = workflowManager.getWorkflow(refreshedSubTask)
        def currentStep = workflow.getLinkedStep(refreshedSubTask.getStatus())

        if(!currentStep){
            logs << "No workflow step '${currentStep}'found for ${refreshedStatus}"
            break
        }


        Collection <ActionDescriptor> availableActions = currentStep.getActions()
        def availableTransitions = availableActions.collect { action ->
            action.getName() 
        }


        if(availableTransitions.any { it.equalsIgnoreCase(toStatus) } ) {
        logs << "Transitioning from status '${fromStatus}' to '${toStatus}' "
        Issues.getByKey(subTaskKey).transition(toStatus)
        
        }
        else {
          logs << "Transition '${toStatus}' not available from '${fromStatus}' "
          break
        } 
    }  
    
}
return logs





