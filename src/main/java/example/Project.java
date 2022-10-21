package example;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("example.org")
@Version("v1alpha1")
@ShortNames("prj")
public class Project extends CustomResource<ProjectSpec, ProjectStatus> implements Namespaced {
}