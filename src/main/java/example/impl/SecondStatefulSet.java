package example.impl;

import example.BaseStatefulSet;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent(labelSelector = SecondStatefulSet.SELECTOR)
public class SecondStatefulSet extends BaseStatefulSet {
    public static final String SELECTOR = "app.kubernetes.io/managed-by=project-operator," +
                                          "app.kubernetes.io/component=second";

    public SecondStatefulSet() {
        super("second");
    }
}