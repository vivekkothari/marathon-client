package mesosphere.marathon.client.model.v2;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by guruprasad.sridharan on 17/12/15.
 */
public class LabelSelectorQuery {
    private String query;

    public LabelSelectorQuery(String query) {
        this.query = query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public static LabelSelectorQueryBuilder builder() {
        return new LabelSelectorQueryBuilder();
    }

    public static class LabelSelectorQueryBuilder {
        private List<QueryElement> queryElements = new ArrayList<>();

        private String constructQuery() {
            String query = "";
            query = queryElements.stream().map(queryElement -> queryElement.stringify())
                    .reduce("", (a, b) -> {
                        if (a.isEmpty()) {
                            return b;
                        } else if (b.isEmpty()) {
                            return a;
                        } else {
                            return a + ", " + b;
                        }

                    });

            try {
                query = URLEncoder.encode(query, "UTF-8");
            } catch (Exception e) {
                throw new RuntimeException("Wrong charset string", e);
            }
            return query;
        }

        public LabelSelectorQueryBuilder addQueryElement(final QueryElement queryElement) {
            queryElements.add(queryElement);
            return this;
        }

        public LabelSelectorQuery build() {
            return new LabelSelectorQuery(constructQuery());
        }

    }

    @Builder
    @NoArgsConstructor
    public static class QueryElement {
        private String lhs;
        private QueryOperator operator;
        @Singular
        private Set<String> rhs = new HashSet<>();

        public QueryElement(final String lhs, final QueryOperator operator, final Set<String> rhs) {
            this.lhs = lhs;
            this.operator = operator;
            this.rhs = rhs;
            validate();
        }

        private void validate() {
            if (lhs == null || lhs.isEmpty()) {
                throw new RuntimeException("QueryElement must have a lhs which is neither null nor empty");
            }
            if (rhs == null) {
                throw new RuntimeException("QueryElement must have a non-null rhs");
            }
            if (rhs.isEmpty() && operator != QueryOperator.EXISTS) {
                throw new RuntimeException("QueryElement must have a non-empty rhs for a non exists operation");
            }
            rhs.stream().forEach(s -> {
                if (s == null || s.isEmpty()) {
                    throw new RuntimeException("QueryElement has a invalid rhs: rhs contains null or empty strings");
                }
            });
            if (rhs.size() > 1 && operator != QueryOperator.IN && operator != QueryOperator.NOT_IN) {
                throw new RuntimeException("QueryElement has a invalid operator: operator must be of type in or notin");
            }

        }

        public String stringify() {
            return lhs.replaceAll("([^A-Za-z0-9-_])", "\\\\$1") + getOperatorString() + getRhsString();
        }

        private String getRhsString() {
            if (rhs.isEmpty()) {
                return "";
            } else if (rhs.size() == 1) {
                return rhs.stream().map(s -> s.replaceAll("([^A-Za-z0-9-_])", "\\\\$1")).reduce("", (a, b) -> a + b);
            }
            String ret = rhs.stream().map(s -> s.replaceAll("([^A-Za-z0-9-_])", "\\\\$1"))
                    .reduce("", (a,b) -> {
                        if (a.isEmpty()) {
                            return b;
                        }else if (b.isEmpty()) {
                            return a;
                        } else {
                            return a + ", " + b;
                        }
                    });
            return "(" + ret + ")";
        }

        private String getOperatorString() {
            switch (operator) {
                case EQUALS:
                    return "==";
                case NOT_EQUALS:
                    return "!=";
                case IN:
                    return " in ";
                case NOT_IN:
                    return " notin ";
                case EXISTS:
                    return "";
                default:
                    return "";
            }
        }

    }

    public enum QueryOperator {
        EQUALS,
        NOT_EQUALS,
        IN,
        NOT_IN,
        EXISTS
    }

}
