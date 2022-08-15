# Mina SSHD Api Plugin

This plugin wraps [Apache Mina SSHD](https://github.com/apache/mina-sshd) modules as Jenkins plugins. Each module have its own plugins.

# Build

To build the plugin locally:

```
mvn clean verify
```

# Release

The release is automated on Pull Request merge as per [this documentation](https://www.jenkins.io/doc/developer/publishing/releasing-cd/#releasing). To release the plugin, ask the maintainer(s) to merge a PR.

[See the release page](https://github.com/jenkinsci/mina-ssh-api-plugin/#releases)

# Test local instance

```
mvn hpi:run
```

# Adding a new Plugin / Module

If in need for a new SSHD module, follow these steps:

Open a pull request in [jenkins-infra/repository-permissions-updater](https://github.com/jenkins-infra/repository-permissions-updater) to have the new plugin added:

* Create a new file `permissions/plugins-mina-sshd-api-#MODULE_NAME.yml` with the following content:

    ```yaml
    ---
    name: "mina-sshd-api-#MODULE_NAME"
    github: &GH "jenkinsci/mina-sshd-api-plugin"
    paths:
    - "io/jenkins/plugins/mina-sshd-api/mina-sshd-api-#MODULE_NAME"
    cd:
      enabled: true
    developers:
    - "jglick"
    - "allan_burdajewicz"
    issues:
      - jira: 28925
    ```

Open a pull request on this repository:

* Create a new directory `mina-sshd-api-#MODULE_NAME`
* Create `pom.xml`
    * Add a dependency on `org.apache.sshd:sshd-#MODULE_NAME`.

        ```
        <dependency>
            <groupId>org.apache.sshd</groupId>
            <artifactId>sshd-#MODULE_NAME</artifactId>
            <version>${revision}</version>
        </dependency>
        ```
      
    * Exclude all transitive dependencies
* Create `src/main/resource/index.jelly`
* Add the module to the root `pom.xml`
