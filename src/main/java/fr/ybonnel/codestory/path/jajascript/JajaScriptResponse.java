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

    public String toJson() {
        StringBuilder builder = new StringBuilder("{\n");
        builder.append("    \"gain\" : ");
        builder.append(gain);
        builder.append(",\n");
        builder.append("    \"path\" : [");
        boolean first = true;
        for (String vol : path) {
            if (!first) {
                builder.append(',');
            }
            builder.append('"');
            builder.append(vol);
            builder.append('"');
            first = false;
        }
        builder.append("]\n}");
        return builder.toString();
    }
}
