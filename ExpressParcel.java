package _CourierSystem;

import java.util.Objects;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType
public final class ExpressParcel extends Parcel {

    @Property
    public final double expressFee;

    @Property
    public final String deliveryTime;

    @Property
    public final boolean priorityHandling;

    public ExpressParcel(
        @JsonProperty("id") final String id,
        @JsonProperty("sender") final String sender,
        @JsonProperty("receiver") final String receiver,
        @JsonProperty("status") final String status,
        @JsonProperty("expressFee") final double expressFee,
        @JsonProperty("deliveryTime") final String deliveryTime,
        @JsonProperty("priorityHandling") final boolean priorityHandling
    ) {
        super(id, sender, receiver, status);
        this.expressFee = expressFee;
        this.deliveryTime = deliveryTime;
        this.priorityHandling = priorityHandling;
    }

    public double getExpressFee() {
        return expressFee;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public boolean isPriorityHandling() {
        return priorityHandling;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expressFee, deliveryTime, priorityHandling);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(hashCode()) + " { " +
               "id=\"" + id + "\", " +
               "sender=\"" + sender + "\", " +
               "receiver=\"" + receiver + "\", " +
               "status=\"" + status + "\", " +
               "expressFee=" + expressFee + ", " +
               "deliveryTime=\"" + deliveryTime + "\", " +
               "priorityHandling=" + priorityHandling + " }";
    }
}
