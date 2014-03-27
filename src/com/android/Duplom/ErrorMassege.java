package com.android.Duplom;

/**
 * Created by bodik on 27.03.14.
 */
public class ErrorMassege {

    private  String statusMessage;
    private  String description;
    private  String networkAction;
    private  String onSiteAction;

    public ErrorMassege(String statusMessage, String description, String networkAction, String onSiteAction) {
        this.statusMessage = statusMessage;
        this.description = description;
        this.networkAction = networkAction;
        this.onSiteAction = onSiteAction;
    }

    /// geters
    public String getStatusMessage() {
        return statusMessage;
    }

    public String getDescription() {
        return description;
    }

    public String getNetworkAction() {
        return networkAction;
    }

    public String getOnSiteAction() {
        return onSiteAction;
    }

    /// seters
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setNetworkAction(String networkAction) {
        this.networkAction = networkAction;
    }

    public void setOnSiteAction(String onSiteAction) {
        this.onSiteAction = onSiteAction;
    }





}
