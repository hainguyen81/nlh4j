/*
 * @(#)TestLicenseTool.java 1.0 May 29, 2015 Copyright 2015 by GNU Lesser General Public License (LGPL).
 * All rights reserved.
 */
package org.nlh4j.license;

import java.io.Serializable;

import org.junit.Test;

import org.nlh4j.util.LicenseUtils;

/**
 * License Generation Tool
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class TestLicenseTool implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	/**
	 * Generate from resource for SystemEXE
	 */
	@Test
	public void generateSysExe() {
	    String path = "D:\\Working\\2.projects\\isev-exex\\isev-license\\src\\main\\resources\\";
	    String[] args = new String[] {
	            "-f",
	            path + "licenseIn.lic",
                "-ks",
                path + ".keystore.jks",
                "-t",
                "JKS",
                "-p",
                "systemexe",
                "-s",
                "Serial.Key",
                "-a",
                "org.nlh4j"
	    };
	    LicenseGenerator.generate(args);
	    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtOt1o3Sj84M66TjMSxKF/fK+iVgNX+Ki+qGjHnFufRVOEkuDmUO9esSxLVcvZ1Rke2TcMMuL95cuOtWCaR+DbsRHOgDkhb5+DBsw0xk/3iX6YQcZcqakQQmjO36VSy/+4WBWacYywCcxGUhbVPyZMIcUHmmSkeke7Dgq5gHooP37JQLSVpfyfU1zCMby11pRjVmP9Xxy5hy33AvpRQehCav3NyCcgcyO0t15vpOE1ID6CM196sbeAZazJKy3aFdBrncDuxWFipE13KUwlvkJdTMOrROEepTSF5iG/XhXgOaf6D3H7kr9duhQqjOebU0kyGKHE/4R6J6Y3fntZCCqmQIDAQAB";
	    if (LicenseUtils.validateRsaLicense(publicKey, path + "licenseIn.out.lic", "Serial.Key", null)) {
            System.out.println("Valid license!");
        } else {
            System.out.println("Invalid license!");
        }
	}
}