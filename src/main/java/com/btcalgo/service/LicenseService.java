package com.btcalgo.service;

import com.license4j.License;
import com.license4j.LicenseValidator;

public class LicenseService {

    private static String publickey = "30819f300d06092a864886f70d010101050003818d003081893032301006072a8648ce3d02002EC311215SHA512withECDSA106052b81040006031e00049c335bf4d4220cc90be16a2154c7c5451fdaa3a9bbe559e003c94359G02818100b56908f7f08f6d2abbf721c6216d07f9d8a91bcdc27ffe467f2250582b5ac562b4c8593bf905c1f4120809e13f9fe0966a2147c4de63f4a55483347bc9296ca1c19a25c4b0a106b5bf5e0c3c56ea4f45a7beb8824b1e1becab7fd62e337a312703RSA4102413SHA512withRSA74f6ef9059c0b1d6e4606ff37189063ec64f6478008de84e943c7b41b538723b0203010001";

    private static String internalString = "1390636638117";
    private static String nameforValidation = null;
    private static String companyforValidation = null;
    private static int hardwareIDMethod = 0;

    public void init() {

    }

    public boolean hasValidLicense() {
        return true;
    }

    public boolean validate(String licenseKey) {

        License license = LicenseValidator.validate(
                "# test_product License (id: 1390649335370)\n" +
                        "d0ea6308195bf8d687b39af1a9c53ff4483742027e3cdca96004e7198013\n" +
                        "81a69ec76372c04204fef4e841048ee2895f20b3eddd68b1b1131208c4e5\n" +
                        "7f9d547bef402765976fafcb2f28cc758e41b7d5f9741f12dc195559dd7f\n" +
                        "8f878af6f41b1779a68353cca7732314622b6c15b9ab993208fb5f861ecf\n" +
                        "0015ec6f6e799aa80fa0e6fc4b2e1636bd9396170bc929de2b6199404564\n" +
                        "ad4b5cc939fd402f0650d643138947291a627c1ac71f56b0ca490f621c64\n" +
                        "aa24999948e37159401ddea6e7e1b0b1b251b960d2b36c53525fa13bb687\n" +
                        "1d65414a909e5254f94e76c47c6c227472640fe5f0ac872f4cc6e439bec0\n" +
                        "3ab455f34a0d567ef8bb8054600cb106226c6e04c494e9a6c18d26c3e52a\n" +
                        "ded6d92737e4dbf69d754f70dbd235b587653a0749622fab5eca40c29763\n" +
                        "9a577302be785742ce8a0fb24c2e85991072414156c596956bfd46560d5d\n" +
                        "4395fabc8f6b3de832db253a6c01de891686d1a1345fe9e7880c85b31c9a\n" +
                        "27c99c99567de5b7d76c160f940aa07b33d43d714784d3132fdfb5f7a689\n" +
                        "28cce6f05f2ee889c1f2e5a161ee0b51c1f4a998f7cb9d0033ebc03badb2\n" +
                        "ebac5d939283491fcf708a44213557f066946d077ccea9a0886569be8df1\n" +
                        "03f6330b8aae6c983866377f7d61ca3fe48b26f0ea7d7ea51821800ce097\n" +
                        "0f00fbd6e96bdc10b80a2fb152342d0e803558fbc96ae76559b288d74772\n" +
                        "79d72b27e85633d55fa2bcfec9c67fadb38507a8139a49e0b323", // REQUIRED - license string
                publickey, // REQUIRED - public key
                // TODO: REPLACE WITH YOUR PRODUCT ID
                "21234", // REQUIRED - product id
                // TODO: REPLACE WITH YOUR PRODUCT EDITION
                null, // product edition if needed
                // TODO: REPLACE WITH YOUR PRODUCT VERSION
                null, // product version if needed
                null, // current date, null for current date
                null); // product release date if needed



        /*License license = LicenseValidator.validate(
                licenseKey,
                publickey,
                internalString,
                nameforValidation,
                companyforValidation,
                hardwareIDMethod);*/
        System.out.println("Validating with all correct parameters: " + license.getValidationStatus());

        System.out.println("1: " + license.getLicenseString());
        System.out.println("1: " + license.getActivationStatus());

        // Auto activate license.
        /*License activatedLicense = LicenseValidator.autoActivate(license);
        System.out.println("License Activation Status: " + activatedLicense.getActivationStatus());

        System.out.println("2: " + activatedLicense.getLicenseString());

        return activatedLicense.getActivationStatus() == ActivationStatus.ACTIVATION_COMPLETED;*/
        return true;
    }

}
