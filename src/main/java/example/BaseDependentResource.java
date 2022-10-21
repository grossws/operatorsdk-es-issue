package example;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

public abstract class BaseDependentResource<R extends HasMetadata> extends CRUDKubernetesDependentResource<R, Project> {
    public static final String K8S_NAME= "app.kubernetes.io/name";
    public static final String K8S_COMPONENT = "app.kubernetes.io/component";

    protected final String component;

    public BaseDependentResource(Class<R> resourceType, String component) {
        super(resourceType);
        this.component = component;
    }

    protected String name(Project primary) {
        return "%s-%s".formatted(component, primary.getSpec().getProjectId());
    }

    protected ObjectMetaBuilder createMeta(Project primary) {
        String name = name(primary);
        return new ObjectMetaBuilder()
                .withName(name)
                .withNamespace(primary.getMetadata().getNamespace())
                .addToLabels(K8S_NAME, name)
                .addToLabels(K8S_COMPONENT, component);
    }
}