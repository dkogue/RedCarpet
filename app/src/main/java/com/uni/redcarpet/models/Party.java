package com.uni.redcarpet.models;

import java.util.Date;

/**
 * Created by cyrille on 30/11/2017.
 */

public class Party {

    public String partyId;
    public String partyOrganiser;
    public String partyDescription;
    public String partyTitle;
    public String partyCity;
    public String partyAddress;
    public Date   partyDate;
    public long   partyTimestamp;
    // Image ...
    public String partyFirebaseToken;

    public Party() {
    }


    public Party(String partyId, String partyOrganiser, String partyDescription,
                 String partyTitle, String partyCity, String partyAddress,
                         Date partyDate,
                 long partyTimestamp, String partyFirebaseToken) {
        this.partyId            = partyId;
        this.partyOrganiser     = partyOrganiser;
        this.partyDescription   = partyDescription;
        this.partyTitle         = partyTitle;
        this.partyCity          = partyCity;
        this.partyAddress       = partyAddress;
        this.partyDate          = partyDate;
        this.partyTimestamp     = partyTimestamp;
        this.partyFirebaseToken = partyFirebaseToken;
    }
}