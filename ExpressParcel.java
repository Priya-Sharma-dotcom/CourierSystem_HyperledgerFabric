package _CourierSystem;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType
public final class ExpressParcel extends Parcel {

    @Property
    public final String deliveryTime;

    @Property
    public final boolean priorityHandling;

    @Property
    public final double expressFee;

    public ExpressParcel(
        @JsonProperty("id") final String id,
        @JsonProperty("sender") final String sender,
        @JsonProperty("receiver") final String receiver,
        @JsonProperty("status") final String status,
        @JsonProperty("deliveryTime") final String deliveryTime,
        @JsonProperty("priorityHandling") final boolean priorityHandling,
        @JsonProperty("expressFee") final double expressFee
    ) {
        super(id, sender, receiver, status);
        this.deliveryTime = deliveryTime;
        this.priorityHandling = priorityHandling;
        this.expressFee = expressFee;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public boolean isPriorityHandling() {
        return priorityHandling;
    }

    public double getExpressFee() {
        return expressFee;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + sender.hashCode() + receiver.hashCode() + .hashCode() + deliveryTime.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(hashCode()) + " { " +
               "id=\"" + id + "\", " +
               "sender=\"" + sender + "\", " +
               "receiver=\"" + receiver + "\", " +
               "status=\"" + status + "\", " +
               "deliveryTime=\"" + deliveryTime + "\", " +
               "priorityHandling=\"" + priorityHandling + "\", " +
               "expressFee=\"" + expressFee + "\" }";
    }
}
