package example.impl;

import example.BaseService;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

@KubernetesDependent(labelSelector = FirstService.SELECTOR)
public class FirstService extends BaseService {
    public static final String SELECTOR = "app.kubernetes.io/managed-by=project-operator," +
                                          "app.kubernetes.io/component=first";

    public FirstService() {
        super("first");
    }
}