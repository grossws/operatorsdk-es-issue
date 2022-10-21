package example.impl;

import example.BaseStatefulSet;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent(labelSelector = FirstStatefulSet.SELECTOR)
public class FirstStatefulSet extends BaseStatefulSet {
    public static final String SELECTOR = "app.kubernetes.io/managed-by=project-operator," +
                                          "app.kubernetes.io/component=first";

    public FirstStatefulSet() {
        super("first");
    }
}