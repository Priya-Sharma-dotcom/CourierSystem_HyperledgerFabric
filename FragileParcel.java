package _CourierSystem;

import java.util.Objects;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType
public final class FragileParcel extends Parcel {

    @Property
    public final String handlingInstructions;

    @Property
    public final boolean extraCushioning;

    public FragileParcel(
        @JsonProperty("id") final String id,
        @JsonProperty("sender") final String sender,
        @JsonProperty("receiver") final String receiver,
        @JsonProperty("status") final String status,
        @JsonProperty("handlingInstructions") final String handlingInstructions,
        @JsonProperty("extraCushioning") final boolean extraCushioning
    ) {
        super(id, sender, receiver, status);
        this.handlingInstructions = handlingInstructions;
        this.extraCushioning = extraCushioning;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public boolean isExtraCushioning() {
        return extraCushioning;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), handlingInstructions, extraCushioning);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(hashCode()) + " { " +
               "id=\"" + id + "\", " +
               "sender=\"" + sender + "\", " +
               "receiver=\"" + receiver + "\", " +
               "status=\"" + status + "\", " +
               "handlingInstructions=\"" + handlingInstructions + "\", " +
               "extraCushioning=" + extraCushioning + " }";
    }
}
