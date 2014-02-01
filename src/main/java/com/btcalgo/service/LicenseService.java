package com.btcalgo.service;

import com.google.common.base.Strings;
import com.license4j.ActivationStatus;
import com.license4j.License;
import com.license4j.LicenseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LicenseService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final int hardwareIDMethod = 0;
    private static final String internalString = "1390636638117";
    private static final String publickey = "30819f300d06092a864886f70d010101050003818d003081893032301006072a8648ce3d02002EC311215SHA512withECDSA106052b81040006031e00049c335bf4d4220cc90be16a2154c7c5451fdaa3a9bbe559e003c94359G02818100b56908f7f08f6d2abbf721c6216d07f9d8a91bcdc27ffe467f2250582b5ac562b4c8593bf905c1f4120809e13f9fe0966a2147c4de63f4a55483347bc9296ca1c19a25c4b0a106b5bf5e0c3c56ea4f45a7beb8824b1e1becab7fd62e337a312703RSA4102413SHA512withRSA74f6ef9059c0b1d6e4606ff37189063ec64f6478008de84e943c7b41b538723b0203010001";
    private static final String companyforValidation = null;
    private static final String nameforValidation = null;

    private volatile boolean validLicense = false;

    private String configFile;

    private static final String LICENSE_KEY_ID = "licenseKey";
    private static final String LICENSE_TEXT_ID = "licenseText";

    private String licenseKey;
    private String licenseText;

    public void init() {
        if (loadLicense()) {
            validateActivatedLicense();
        }
    }

    public boolean hasValidLicense() {
        return validLicense;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public boolean activateLicense(String licenseKey) {
        License license = LicenseValidator.validate(
                licenseKey,
                publickey,
                internalString,
                nameforValidation,
                companyforValidation,
                hardwareIDMethod);
        log.info("Validating license... Validation Status: {}", license.getValidationStatus());

        License activatedLicense = LicenseValidator.autoActivate(license);
        log.info("Activating license... Activation Status: {}", activatedLicense.getActivationStatus());

        if (activatedLicense.getActivationStatus() == ActivationStatus.ACTIVATION_COMPLETED) {
            validLicense = true;
            this.licenseKey = licenseKey;
            this.licenseText = activatedLicense.getLicenseString();
            saveLicense();
            return validLicense;
        } else {
            return false;
        }
    }

    public void validateActivatedLicense() {
        License license = LicenseValidator.validate(
                licenseText,
                publickey, // REQUIRED - public key
                "21234", // REQUIRED - product id
                null, // product edition if needed
                null, // product version if needed
                null, // current date, null for current date
                null); // product release date if needed
        log.info("Checking stored license... Validation Status: {}", license.getValidationStatus());
        log.info("Checking stored license... Activation Status: {}", license.getActivationStatus());

        validLicense = license.getActivationStatus() == ActivationStatus.ACTIVATION_COMPLETED;
    }

    public void saveLicense() {
        if (Strings.isNullOrEmpty(licenseKey) || Strings.isNullOrEmpty(licenseText)) {
            log.error("Unable to store valid license. 'licenseKey' or 'licenseText' is null");
        }

        try {
            Properties properties = new Properties();
            properties.setProperty(LICENSE_KEY_ID, licenseKey);
            properties.setProperty(LICENSE_TEXT_ID, licenseText);
            properties.store(new FileOutputStream(configFile, false), null);
            log.info("Valid license info successfully stored into {}", configFile);
        } catch (IOException e) {
            log.error("Exception during saving valid license: ", e);
        }
    }

    public boolean loadLicense() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(configFile));
            licenseKey = properties.getProperty(LICENSE_KEY_ID);
            licenseText = properties.getProperty(LICENSE_TEXT_ID);

            return !Strings.isNullOrEmpty(licenseKey) && !Strings.isNullOrEmpty(licenseText);
        } catch (IOException e) {
            log.error("Exception during license loading: ", e);
            return false;
        }
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
