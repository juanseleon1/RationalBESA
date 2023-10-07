package rational.guards;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Exception.ExceptionBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Log.ReportBESA;
import java.util.Iterator;
import rational.RationalRole;
import rational.RationalState;
import rational.mapping.Plan;
import rational.mapping.Task;

/**
 * A class representing a guard that handles the change of a rational agent's
 * role. This class extends GuardBESA.
 */
public class ChangeRationalRoleGuard extends GuardBESA {

    @Override
    public void funcExecGuard(EventBESA ebesa) {
        RationalState state = (RationalState) this.getAgent().getState();
        RationalRole newrole = (RationalRole) ebesa.getData();
        boolean sendUpdate = false;
        if (state.getMainRole() != null) {
            ReportBESA.debug("🟢 MainRole " + state.getMainRole().getRoleName() + " 🟢 Trying to change to rol 🟩 "
                    + newrole.getRoleName());
        } else {
            ReportBESA.debug("🟢🟢 New Rol " + newrole.getRoleName());
        }

        if (state.getMainRole() != null
                && !state.getMainRole().getRoleName().equals(((RationalRole) ebesa.getData()).getRoleName())) {
            sendUpdate = true;
            ReportBESA.debug("History Recording Start");
            state.recordReasoningHistory();
            ReportBESA.debug("History Recording end");
            Plan plan = state.getMainRole().getRolePlan();
            if (plan != null) {
                Iterator<Task> it = plan.getTasksInExecution().iterator();
                ReportBESA.debug("🔸 " + plan.getTasks().size() + " plans " + plan.getTasks());
                while (it.hasNext()) {
                    Task task = it.next();
                    ReportBESA.debug("Revisando si la tarea está en ejecución: " + task.toString());
                    if (task.isInExecution()) {
                        ReportBESA.debug("Tarea en ejecución: " + task.getClass().getSimpleName().toString());
                        task.cancelTask(state.getBelieves());
                        it.remove();
                    } else if (task.isFinalized()) {
                        it.remove();
                    }
                    task.setTaskFinalized();
                }
            }
            newrole.resetPlan();
            state.setMainRole(newrole);
        } else if (state.getMainRole() == null) {
            sendUpdate = true;
            ReportBESA.debug("NUEVO ROL ASIGNADO");
            ReportBESA.debug("History Recording Start");
            state.recordReasoningHistory();
            ReportBESA.debug("History Recording end");
            newrole.resetPlan();
            state.setMainRole(newrole);
        } else if (!state.getMainRole().getRolePlan().inExecution()) {
            ReportBESA.debug("NO HAY NADA EJECUTANDOSE");
            ReportBESA.debug(
                    "getTasksWaitingForExecution " + state.getMainRole().getRolePlan().getTasksWaitingForExecution());
            ReportBESA.debug("getTasks " + state.getMainRole().getRolePlan().getTasks());
            state.getMainRole().getRolePlan().reset();
        }
        if (sendUpdate) {
            AgHandlerBESA handler;
            try {
                ReportBESA.debug("Sending update.");
                handler = AdmBESA.getInstance().getHandlerByAid(this.getAgent().getAid());
                handler.sendEvent(new EventBESA(PlanExecutionGuard.class.getName()));
            } catch (ExceptionBESA e) {
                e.printStackTrace();
            }
        }
    }

}
