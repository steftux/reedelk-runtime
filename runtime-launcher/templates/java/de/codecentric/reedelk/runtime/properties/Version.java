package de.codecentric.reedelk.runtime.properties;

/**
 * WARNING: Do not touch this file. It is autogenerated every
 * time a new build is executed.
 */
public final class Version {

    private static final String VERSION = "${project.version}";
    private static final String GROUPID = "${project.groupId}";
    private static final String ARTIFACTID = "${project.artifactId}";
    private static final String QUALIFIER = "${version.qualifier}";

    public static String getVersion() {
        return VERSION;
    }

    public static String getGroupId() {
        return GROUPID;
    }

    public static String getArtifactId() {
        return ARTIFACTID;
    }

    public static String getQualifier() {
        return QUALIFIER;
    }

}
