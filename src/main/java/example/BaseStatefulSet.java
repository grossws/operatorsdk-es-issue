package example;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.javaoperatorsdk.operator.ReconcilerUtils;
import io.javaoperatorsdk.operator.api.reconciler.Context;

import java.util.Map;

public abstract class BaseStatefulSet extends BaseDependentResource<StatefulSet> {
    public BaseStatefulSet(String component) {
        super(StatefulSet.class, component);
    }

    @Override
    protected StatefulSet desired(Project primary, Context<Project> context) {
        var template = ReconcilerUtils.loadYaml(StatefulSet.class, getClass(), "/statefulset.yaml");
        var name = name(primary);
        var metadata = createMeta(primary).build();

        return new StatefulSetBuilder(template)
                .withMetadata(metadata)
                .editSpec()
                .withServiceName(name)
                .editOrNewSelector()
                .withMatchLabels(Map.of(K8S_NAME, name))
                .endSelector()
                .editTemplate()
                .withMetadata(metadata)
                .endTemplate()
                .editFirstVolumeClaimTemplate()
                .editMetadata()
                .withLabels(Map.of(K8S_NAME, name))
                .endMetadata()
                .endVolumeClaimTemplate()
                .endSpec()
                .build();
    }
}