package _CourierSystem;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

import java.util.Objects;

@DataType
public final class InternationalParcel extends Parcel {

    @Property
    public final String originCountry;

    @Property
    public final String destinationCountry;

    @Property
    public final String customsDeclaration;

    public InternationalParcel(
        @JsonProperty("id") final String id,
        @JsonProperty("sender") final String sender,
        @JsonProperty("receiver") final String receiver,
        @JsonProperty("status") final String status,
        @JsonProperty("originCountry") final String originCountry,
        @JsonProperty("destinationCountry") final String destinationCountry,
        @JsonProperty("customsDeclaration") final String customsDeclaration
    ) {
        super(id, sender, receiver, status);
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
        this.customsDeclaration = customsDeclaration;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public String getCustomsDeclaration() {
        return customsDeclaration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            originCountry,
            destinationCountry,
            customsDeclaration
        );
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(hashCode()) + " { " +
               "id=\"" + id + "\", " +
               "sender=\"" + sender + "\", " +
               "receiver=\"" + receiver + "\", " +
               "status=\"" + status + "\", " +
               "originCountry=\"" + originCountry + "\", " +
               "destinationCountry=\"" + destinationCountry + "\", " +
               "customsDeclaration=\"" + customsDeclaration + "\" }";
    }
}
