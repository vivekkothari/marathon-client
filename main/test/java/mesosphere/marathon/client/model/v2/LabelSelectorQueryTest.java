package mesosphere.marathon.client.model.v2;

import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.MarathonClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by guruprasad.sridharan on 18/12/15.
 */
public class LabelSelectorQueryTest {

    @Test
    public void testQueryBuilder() {
        Set<String> set1 = new HashSet<>();
        set1.add("us");
        set1.add("eu");
        Set<String> set2 = new HashSet<>();
        set2.add("aa");
        set2.add("bb");

        String query = LabelSelectorQuery.builder()
                .addQueryElement(LabelSelectorQuery.QueryElement.builder()
                        .lhs("environment")
                        .operator(LabelSelectorQuery.QueryOperator.EQUALS)
                        .rhs(Collections.singleton("production"))
                        .build())
                .addQueryElement(LabelSelectorQuery.QueryElement.builder()
                        .lhs("tier")
                        .operator(LabelSelectorQuery.QueryOperator.NOT_EQUALS)
                        .rhs(Collections.singleton("frontend tier"))
                        .build())
                .addQueryElement(LabelSelectorQuery.QueryElement.builder()
                        .lhs("deployed")
                        .operator(LabelSelectorQuery.QueryOperator.IN)
                        .rh("us")
                        .rh("eu")
                        .build())
                .addQueryElement(LabelSelectorQuery.QueryElement.builder()
                        .lhs("deployed")
                        .operator(LabelSelectorQuery.QueryOperator.NOT_IN)
                        .rh("aa")
                        .rh("bb")
                        .build())
                .build().getQuery();

        Assert.assertTrue(query.equals("environment%3D%3Dproduction%2C+tier%21%3Dfrontend%5C+tier%2C+deployed+in+%28us%2C+eu%29%2C+deployed+notin+%28aa%2C+bb%29"));
    }

    /*@Test
    public void testGetApps() {
        Marathon marathon = MarathonClient.getInstance("http://dcos-elasticlb-900120833.ap-southeast-1.elb.amazonaws.com/service/marathon/");
        System.out.println(marathon.getApps(LabelSelectorQuery.builder()
                .addQueryElement(LabelSelectorQuery.QueryElement.builder()
                        .lhs("DCOS_PACKAGE_RELEASE")
                        .operator(LabelSelectorQuery.QueryOperator.EXISTS)
                        .build())
                .build().getQuery()));
    }*/

}
