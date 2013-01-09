package fr.ybonnel.codestory.query;


import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ChangeScalaskelQueryHandler extends AbstractQueryHandler {
    @Override
    public String getResponse(String query, String path, HttpServletRequest request) throws Exception {
        return new GsonBuilder().create().toJson(getChanges(1));
    }

    private List<Change> getChanges(int cents) {
        return Lists.newArrayList(new Change(1, null, null, null));
    }

    private class Change {
        private Integer foo;
        private Integer bar;
        private Integer qix;
        private Integer baz;

        private Change(Integer foo, Integer bar, Integer qix, Integer baz) {
            this.foo = foo;
            this.bar = bar;
            this.qix = qix;
            this.baz = baz;
        }
    }
}
