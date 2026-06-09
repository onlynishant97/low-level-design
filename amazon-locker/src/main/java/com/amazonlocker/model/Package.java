package com.amazonlocker.model;

import com.amazonlocker.enums.PackageStatus;
import com.amazonlocker.enums.Size;
import lombok.Data;

@Data
public class Package {
    String packageId;
    String orderId;
    String customerId;
    Size size;
    PackageStatus status;
    Compartment assignedCompartment;

    public Package(String packageId, String orderId, String customerId, Size size) {
        this.packageId = packageId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.size = size;
        this.status = PackageStatus.ASSIGNED;
    }
}
