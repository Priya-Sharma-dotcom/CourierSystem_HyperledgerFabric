package _CourierSystem;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType
public final class StandardParcel extends Parcel {

    @Property
    public final String deliveryDate;

    public StandardParcel(
        @JsonProperty("id") final String id,
        @JsonProperty("sender") final String sender,
        @JsonProperty("receiver") final String receiver,
        @JsonProperty("status") final String status,
        @JsonProperty("deliveryDate") final String deliveryDate
    ) {
        super(id, sender, receiver, status);
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(hashCode()) + " { " +
               "id=\"" + id + "\", " +
               "sender=\"" + sender + "\", " +
               "receiver=\"" + receiver + "\", " +
               "status=\"" + status + "\", " +
               "deliveryDate=\"" + deliveryDate + "\" }";
    }
}
