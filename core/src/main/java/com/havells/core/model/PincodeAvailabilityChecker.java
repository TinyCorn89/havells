package com.havells.core.model;

import java.io.Serializable;

public class PincodeAvailabilityChecker implements Serializable {
    private String pincode;
    private boolean cod;

    public enum Status {
        unavailable,
        available
    }

    public PincodeAvailabilityChecker() {
    }

    public PincodeAvailabilityChecker(String pincode, boolean cod) {
        this.pincode = pincode;
        this.cod = cod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }
}
