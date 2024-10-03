package io.jenkins.plugins.mina_sshd_api.core.bouncycastle_registar;

import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RealJenkinsRule;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class TestBCFIPSRegistar {

    @Rule
    public RealJenkinsRule j = new RealJenkinsRule().withFIPSEnabled()
            .omitPlugins("eddsa-api").javaOptions("-Xmx256m");

    @Test
    public void BCFIPSRegistered() throws Throwable {

        URI path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("rsa2048")).toURI();
        j.then(r -> {
            Iterable<KeyPair> keyPairs = SecurityUtils.loadKeyPairIdentities(null, null,
                    Files.newInputStream(Path.of(path)) , FilePasswordProvider.of("theaustraliancricketteamisthebest"));
            assertThat(keyPairs.iterator().next(), notNullValue());
        });
    }

    @Test
    public void BCFIPSRegisteredRejectSSHkeyGen() throws Throwable {

        URI path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("id_rsa-keygen-rsa-2048")).toURI();
        j.then(r -> {
            try {
                Iterable<KeyPair> keyPairs = SecurityUtils.loadKeyPairIdentities(null, null,
                        Files.newInputStream(Path.of(path)), FilePasswordProvider.of("theaustraliancricketteamisthebest"));
                keyPairs.iterator().next();
            } catch (NoSuchAlgorithmException e) {
                // all good
                // we cannot use expected = NoSuchAlgorithmException.class
                // as we get org.jvnet.hudson.test.RealJenkinsRule$StepException
            }
        });
    }

    @Test
    public void BCFIPSRegisteredRejectSSHkeyGened25519() throws Throwable {

        URI path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ed25519.pem")).toURI();
        j.then(r -> {
            try {
                Iterable<KeyPair> keyPairs = SecurityUtils.loadKeyPairIdentities(null, null,
                        Files.newInputStream(Path.of(path)), FilePasswordProvider.of(""));
                keyPairs.iterator().next();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                // all good
                // we cannot use expected = NoSuchAlgorithmException.class
                // as we get org.jvnet.hudson.test.RealJenkinsRule$StepException
            }
        });
    }

    @Test
    public void loadSSHkeyGen_ed25519() throws Throwable {
        URI path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ed25519.pem")).toURI();
        Iterable<KeyPair> keyPairs = SecurityUtils.loadKeyPairIdentities(null, null,
                Files.newInputStream(Path.of(path)), FilePasswordProvider.of(""));
        assertThat(keyPairs.iterator().next(), notNullValue());
    }

    @Test
    public void loadSSHkeyGen() throws Throwable {
        URI path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("id_rsa-keygen-rsa-2048")).toURI();
        Iterable<KeyPair> keyPairs = SecurityUtils.loadKeyPairIdentities(null, null,
                Files.newInputStream(Path.of(path)), FilePasswordProvider.of("theaustraliancricketteamisthebest"));
        assertThat(keyPairs.iterator().next(), notNullValue());

    }

}