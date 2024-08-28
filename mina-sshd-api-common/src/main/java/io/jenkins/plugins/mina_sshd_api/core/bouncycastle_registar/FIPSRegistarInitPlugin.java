/*
 * The MIT License
 *
 * Copyright (c) 2024, Olivier Lamy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.jenkins.plugins.mina_sshd_api.core.bouncycastle_registar;

import hudson.Plugin;
import jenkins.security.FIPS140;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import java.security.Security;
import java.util.logging.Logger;

/**
 * In FIPS environment, Apache Mina needs to be tailored with a custom setup to enable FIPS mode
 * and register BCFIPS as security provider
 *
 * This can be done via System properties but needs to be done BEFORE any Mina classes have been loaded
 */
@Restricted(NoExternalUse.class)
public class FIPSRegistarInitPlugin extends Plugin {

    private static final Logger LOG = Logger.getLogger(FIPSRegistarInitPlugin.class.getName());
    @Override
    public void start() throws Exception {
        if(FIPS140.useCompliantAlgorithms() && Security.getProvider("BCFIPS") != null) {
            LOG.info("register enable FIPS mode for Apache Mina and register defaultProvider as BCFIPS");
            System.setProperty("org.apache.sshd.security.fipsEnabled", "true");
            System.setProperty("org.apache.sshd.security.defaultProvider", "BCFIPS");
        } else {
            LOG.config("not needed to register FIPSBouncyCastleSecurityProviderRegistar");
        }
    }
}
