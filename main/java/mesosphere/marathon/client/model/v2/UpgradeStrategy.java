package mesosphere.marathon.client.model.v2;

import mesosphere.marathon.client.utils.ModelUtils;

/**
 * Created by guruprasad.sridharan on 27/11/15.
 */
public class UpgradeStrategy {
    private Double minimumHealthCapacity;
    private Double maximumOverCapacity;

    public UpgradeStrategy(Double minimumHealthCapacity, Double maximumOverCapacity) {
        this.minimumHealthCapacity = minimumHealthCapacity;
        this.maximumOverCapacity = maximumOverCapacity;
    }

    public Double getMinimumHealthCapacity() { return minimumHealthCapacity; }
    public void setMinimumHealthCapacity(Double minimumHealthCapacity) { this.minimumHealthCapacity = minimumHealthCapacity; }
    public Double getMaximumOverCapacity() { return maximumOverCapacity; }
    public void setMaximumOverCapacity(Double maximumOverCapacity) { this.maximumOverCapacity = maximumOverCapacity; }

    @Override
    public String toString() { return ModelUtils.toString(this); }
}
