package com.is.IS_4k1_TFI_G2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public enum Estado {
    ACTIVO,
    SUSPENDIDO;
}
