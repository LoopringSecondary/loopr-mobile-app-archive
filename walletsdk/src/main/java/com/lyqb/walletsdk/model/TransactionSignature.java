package com.lyqb.walletsdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionSignature {

    private String v;

    private String r;

    private String s;
}
