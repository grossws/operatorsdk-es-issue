package example;

import example.impl.FirstService;
import example.impl.FirstStatefulSet;
import example.impl.SecondService;
import example.impl.SecondStatefulSet;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.api.model.ListOptionsBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import io.javaoperatorsdk.operator.processing.dependent.workflow.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Objects;

@ControllerConfiguration(
        name = "project-operator",
        dependents = {
                @Dependent(name = "first-svc", type = FirstService.class),
                @Dependent(name = "second-svc", type = SecondService.class),
                @Dependent(name = "first", type = FirstStatefulSet.class,
                        dependsOn = {"first-svc"},
                        readyPostcondition = MyReconciler.FirstReadyCondition.class),
                @Dependent(name = "second", type = SecondStatefulSet.class,
                        dependsOn = {"second-svc", "first"}),
        }
)
public class MyReconciler implements Reconciler<Project>, ContextInitializer<Project> {
    public static final Logger log = LoggerFactory.getLogger(MyReconciler.class);

    @Inject
    KubernetesClient client;

    @Override
    public void initContext(Project primary, Context<Project> context) {
        context.managedDependentResourceContext().put("client", client);
    }

    @Override
    public UpdateControl<Project> reconcile(Project resource, Context<Project> context) throws Exception {
        var ready = context.managedDependentResourceContext().getWorkflowReconcileResult().orElseThrow().allDependentResourcesReady();

        var status = Objects.requireNonNullElseGet(resource.getStatus(), ProjectStatus::new);
        status.setStatus(ready ? "ready" : "not-ready");
        resource.setStatus(status);

        return UpdateControl.updateStatus(resource).rescheduleAfter(Duration.ofSeconds(10));
    }

    public static class FirstReadyCondition implements Condition<StatefulSet, Project> {
        @Override
        public boolean isMet(Project primary, StatefulSet secondary, Context<Project> context) {
            var client = context.managedDependentResourceContext().getMandatory("client", KubernetesClient.class);

            var options = new ListOptionsBuilder().withLabelSelector("app.kubernetes.io/name=" + secondary.getMetadata().getName()).build();
            var statefulSets = client.resources(StatefulSet.class).list(options);
            if (!statefulSets.getItems().isEmpty()) {
                log.info("secondary status: {}", secondary.getStatus());
                log.info("fetched status: {}", statefulSets.getItems().get(0).getStatus());
            }

            var readyReplicas = secondary.getStatus().getReadyReplicas();
            return readyReplicas != null && readyReplicas > 0;
        }
    }
}