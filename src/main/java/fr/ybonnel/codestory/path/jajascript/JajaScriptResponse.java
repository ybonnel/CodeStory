package fr.ybonnel.codestory.path.jajascript;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JajaScriptResponse {

    @JsonProperty
    private int gain;
    @JsonProperty
    private List<String> path;

    public JajaScriptResponse(int gain, List<String> path) {
        this.gain = gain;
        this.path = path;
    }
}
