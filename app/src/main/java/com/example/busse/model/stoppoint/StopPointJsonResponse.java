package com.example.busse.model.stoppoint;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopPointJsonResponse {
    @SerializedName("body")
    private List<StopPointBody> stopPointBody;

    public List<StopPointBody> getStopPointBody() {
        return stopPointBody;
    }
}
